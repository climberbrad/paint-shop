package com.bjordan.paintshop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
  public static final String GLOSS = "G";
  public static final String MATTE = "M";

  private final String customerDataFile;


  /**
   * Iterate through customers and put into two buckets -> 1: single PaintOption 2: multiple PaintOptions
   * Default a PaintOption[] solution with GLOSS and MUTABLE PaintOptions
   * First iterate over single PaintOption customers adding their PaintOptions to solution[] setting their PaintOption as immutable.
   * Iterate over multiple PaintOption customers, trying to match GLOSS first, then MATTE if available
   */
  public App(String filePath) {
    this.customerDataFile = filePath;
  }

  public PaintOption[] mixPaint() throws IOException {
    // parse input file
    BufferedReader reader = new BufferedReader(new FileReader(customerDataFile));
    int numColors = Integer.valueOf(reader.readLine());

    // initialize the PaintOption solution array with all GLOSS
    PaintOption[] solution = new PaintOption[numColors];
    for (int i = 0; i < numColors; i++) {
      solution[i] = new PaintOption(i, GLOSS, true);
    }

    // build customer list into either SINGLE or MULTI_OPTION CustomerTypes
    Map<Customer.CustomerType, List<Customer>> customers = parseCustomerData(reader);

    // populate the solution with immutable PainOptions from customer with only one choice
    matchSinglePaintOptionCustomers(customers, solution);

    // try and match multi-option customers: GLOSS first, then MATTE if no other option exists
    for (Customer multiOptionCustomer : customers.get(Customer.CustomerType.MULTI_OPTION)) {

      List<PaintOption> glossOptions =
          multiOptionCustomer.paintOptions.stream()
              .filter(paintOption -> paintOption.paintFinish.equalsIgnoreCase(GLOSS))
              .collect(Collectors.toList());
      boolean match = matchMultiOptionCustomer(glossOptions, solution);

      if (match) {
        continue;
      }

      List<PaintOption> matteOptions =
          multiOptionCustomer.paintOptions.stream()
              .filter(paintOption -> paintOption.paintFinish.equalsIgnoreCase(MATTE))
              .collect(Collectors.toList());
      match = matchMultiOptionCustomer(matteOptions, solution);

      if (!match) {
        printError(multiOptionCustomer);
        return new PaintOption[]{};
      }
    }

    return solution;
  }

  /**
   * Step 1:
   * Initialize a PaintOption array with all GLOSS. Take customers with a single PaintOption and fill the array marking
   * the PaintOption as immutable since this customer has no other PaintOptions.
   * @param customers the entire customer list
   * @param solution the existing PaintOption array initialized as all GLOSS
   */
  private PaintOption[] matchSinglePaintOptionCustomers(Map<Customer.CustomerType, List<Customer>> customers, PaintOption[] solution) {

    // set single single option customers paint as immutable
    for (Customer singleOptionCustomer : customers.get(Customer.CustomerType.SINGLE_OPTION)) {
      PaintOption customerPaintOption = singleOptionCustomer.paintOptions.get(0);
      int colorIndex = customerPaintOption.colorIndex;
      String customerPaintFinish = customerPaintOption.paintFinish;

      if (!solution[colorIndex - 1].paintFinish.equals(customerPaintFinish) && !solution[colorIndex - 1].mutable) {
        printError(singleOptionCustomer);
        return new PaintOption[] {};
      }
      solution[colorIndex - 1] = customerPaintOption;
    }
    return solution;
  }

  /**
   * Step 2
   * Customers with multiple paint options will choose from the PaintOptions available after Step 1 defaulting to GLOSS when possible.
   *
   * @param paintOptions the PaintOption array after step 1 has modified it.
   * @param solution
   * @return
   */
  private boolean matchMultiOptionCustomer(List<PaintOption> paintOptions, PaintOption[] solution) {

    for (PaintOption customerGlossOption : paintOptions) {
      String solutionPaintFinish = solution[customerGlossOption.colorIndex - 1].paintFinish;

      if (solutionPaintFinish.equalsIgnoreCase(customerGlossOption.paintFinish)) {
        return true;
      } else if (!solutionPaintFinish.equalsIgnoreCase(customerGlossOption.paintFinish) && solution[customerGlossOption.colorIndex - 1].mutable) {
        solution[customerGlossOption.colorIndex - 1] = customerGlossOption;
        return true;
      }
    }
    return false;
  }

  /**
   * Create two lists of customers: those with multiple color options and those with only one
   * @param reader input file reader
   * @return a map of Customer.MULTI_OPTION and Customer.SINGLE_OPTION
   * @throws IOException
   */
  private Map<Customer.CustomerType, List<Customer>> parseCustomerData(BufferedReader reader) throws IOException {
    List<Customer> singleOptionCustomers = new ArrayList<>();
    List<Customer> multipleOptionCustomers = new ArrayList<>();

    String line;
    while ((line = reader.readLine()) != null) {
      String regex = "\\d\\s\\w"; // match on "1 G" as an example
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(line);

      List<PaintOption> paintOptions = new ArrayList<>();
      while (matcher.find()) {
        String[] split = matcher.group().split("\\s");

        Integer colorIndex = Integer.valueOf(split[0]);
        String paintFinish = split[1];

        paintOptions.add(new PaintOption(colorIndex, paintFinish, true));
      }

      // split customers into single option and multi-option
      if (paintOptions.size() == 1) {
        paintOptions.get(0).setMutable(false); // hack due to matcher not having a count()
        singleOptionCustomers.add(new Customer(Arrays.asList(paintOptions.get(0))));
      } else {
        multipleOptionCustomers.add(new Customer(paintOptions));
      }
    }

    Map<Customer.CustomerType, List<Customer>> customerMap = new HashMap<>();
    customerMap.put(Customer.CustomerType.SINGLE_OPTION, singleOptionCustomers);
    customerMap.put(Customer.CustomerType.MULTI_OPTION, multipleOptionCustomers);

    return customerMap;
  }

  private void printError(Customer multiOptionCustomer) {
    System.out.println("Impossible to satisfy " + multiOptionCustomer);
  }

  public static void printPaintOptions(PaintOption[] results) {
    for (PaintOption option : results) {
      System.out.print(option.paintFinish + " ");
    }
    System.out.println("");
  }

  public static void main(String[] args) {
    App app = new App(args[0]);
    PaintOption[] solution = new PaintOption[0];
    try {
      solution = app.mixPaint();
    } catch (IOException e) {
      e.printStackTrace();
    }
    printPaintOptions(solution);
  }
}
