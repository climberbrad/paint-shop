package com.bjordan.paintshop;

import static com.bjordan.paintshop.Customer.CustomerType.MULTI_OPTION;
import static com.bjordan.paintshop.Customer.CustomerType.SINGLE_OPTION;
import static com.bjordan.paintshop.PaintFinish.GLOSS;
import static com.bjordan.paintshop.PaintFinish.MATTE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
  public static final PaintOption[] EMPTY_PAINT_OPTION = {};

  private final Map<Customer.CustomerType, List<Customer>> customers;
  private int numColors = 0;

  /**
   * Iterate through customers and put into two buckets -> 1: single PaintOption 2: multiple PaintOptions
   * Default a PaintOption[] solution with GLOSS and MUTABLE PaintOptions
   * First iterate over single PaintOption customers adding their PaintOptions to solution[] setting their PaintOption as immutable.
   * Iterate over multiple PaintOption customers, trying to match GLOSS first, then MATTE if available
   */
  public App(String filePath) {
    Map<Customer.CustomerType, List<Customer>> customers = readDataFile(filePath);
    this.customers = customers;
  }

  /**
   * Allow access to the customer map so we can print their final PaintOption choice.
   * @return customer map.
   */
  public Map<Customer.CustomerType, List<Customer>> getCustomers() {
    return Collections.unmodifiableMap(customers);
  }

  /**
   * Start with a list of PaintOptions set the the number read in from the data file.
   * Initialize the list with all GLOSS options.
   * Match customers with only a single option first.
   * Next match customers with  multiple options trying GLOSS first and then MATTE.
   *
   * @return An array of PaintOption[] which satisfy all customers.
   */
  public PaintOption[] mixPaint() {
    if (customers.isEmpty()) {
      return EMPTY_PAINT_OPTION;
    }

    // initialize the PaintOption solution array with all GLOSS
    PaintOption[] solution = new PaintOption[numColors];
    for (int i = 0; i < numColors; i++) {
      solution[i] = new PaintOption(i, GLOSS, true);
    }

    boolean matched = false;
    if (!customers.get(SINGLE_OPTION).isEmpty()) {
      matched = matchSinglePaintOptionCustomers(customers.get(SINGLE_OPTION), solution);
      if (!matched) {
        return EMPTY_PAINT_OPTION;
      }
    }

    if (!customers.get(MULTI_OPTION).isEmpty()) {
      matched = matchMultiPaintOptionCustomer(customers.get(MULTI_OPTION), solution);
      if (!matched) {
        return EMPTY_PAINT_OPTION;
      }
    }

    return solution;
  }

  /**
   * Read in data file creating the customer list. Split the list into two kinds of customers:
   *  1. SINGLE_OPTION customers
   *  2. MULTI_OPTION customers
   *
   * @return the list of customers by type
   */
  private Map<Customer.CustomerType, List<Customer>> readDataFile(String filePath) {
    Map<Customer.CustomerType, List<Customer>> customers = new HashMap<>();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(filePath));
      numColors = Integer.valueOf(reader.readLine());

      // build customer list into either SINGLE or MULTI_OPTION CustomerTypes
      customers = parseCustomerData(reader);
    } catch (IOException exception) {
      System.out.println("Unable to read in file: " + filePath + " " + exception.getMessage());
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return customers;
  }

  /**
   * Step 1:
   * Take customers with a single PaintOption and fill the array marking
   * the PaintOption as immutable because the customer has no other PaintOption.
   *
   * @param singleOptionCustomers SINGLE_OPTION customer list.
   * @param solution the existing PaintOption array initialized as all GLOSS
   */
  private boolean matchSinglePaintOptionCustomers(List<Customer> singleOptionCustomers, PaintOption[] solution) {
    for (Customer singleOptionCustomer : singleOptionCustomers) {
      PaintOption customerPaintOption = singleOptionCustomer.paintOptions.get(0);
      int colorIndex = customerPaintOption.colorIndex;

      if (!solution[colorIndex - 1].paintFinish.equals(customerPaintOption.paintFinish) && !solution[colorIndex - 1].mutable) {
        printError(singleOptionCustomer);
        return false;
      }
      solution[colorIndex - 1] = customerPaintOption;
      singleOptionCustomer.setChosenOption(customerPaintOption);
    }
    return true;
  }

  /**
   * Step 2:
   * Customers with multiple options should try and match GLOSS first
   * and then MATTE if the option is mutable on the PaintOption[] solution.
   *
   * @param multiOptionCustomers customers with multiple PaintOption(s)
   * @param solution the existing paint options already modified in Step 1 by SINGLE_OPTION customers.
   */
  private boolean matchMultiPaintOptionCustomer(List<Customer> multiOptionCustomers, PaintOption[] solution) {
    boolean matched = false;
    for (Customer customer : multiOptionCustomers) {
      List<PaintOption> glossOptions =
          customer.paintOptions.stream()
              .filter(paintOption -> paintOption.paintFinish == GLOSS)
              .collect(Collectors.toList());
      matched = matchOrSetFinish(customer, glossOptions, solution);

      if (matched) {
        continue;
      }

      List<PaintOption> matteOptions =
          customer.paintOptions.stream()
              .filter(paintOption -> paintOption.paintFinish == MATTE)
              .collect(Collectors.toList());
      matched = matchOrSetFinish(customer, matteOptions, solution);

      if (!matched) {
        printError(customer);
      }
    }
    return matched;
  }

  /**
   * Either match an available options or change a mutable option in the PaintOption[] solution.
   *
   * @param customer the customer we are matching for.
   * @param paintOptions either GLOSS OR MATTE finish options.
   * @param solution the PaintOption solution which has been mutated in Step 1.
   * @return false if there is no possible match.
   */
  private boolean matchOrSetFinish(Customer customer, List<PaintOption> paintOptions, PaintOption[] solution) {

    for (PaintOption customerOption : paintOptions) {
      PaintFinish solutionPaintFinish = solution[customerOption.colorIndex - 1].paintFinish;

      if (solutionPaintFinish == customerOption.paintFinish) {
        customer.setChosenOption(customerOption);
        return true;
      } else if ((solutionPaintFinish != customerOption.paintFinish) && solution[customerOption.colorIndex - 1].mutable) {
        solution[customerOption.colorIndex - 1] = customerOption;
        customer.setChosenOption(customerOption);
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
      String regex = "\\d?\\d\\s\\w"; // match on "1 G" as an example
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(line);


      List<PaintOption> paintOptions = new ArrayList<>();
      List<MatchResult> matches = matcher.results().collect(Collectors.toList());
      int numOptions = matches.size();
      for (MatchResult match : matches) {
        String[] split = match.group().split("\\s");
        Integer colorIndex = Integer.valueOf(split[0]);
        if (colorIndex > numColors) {
          throw new IOException("Incorrect color index: " + colorIndex + " The total number of available colors is: " + numColors);
        }
        String finishLetter = split[1];

        PaintFinish paintFinish = PaintFinish.fromString(finishLetter);
        if (paintFinish == null) {
          throw new IOException("Invalid paint option: " + finishLetter);
        }

        paintOptions.add(new PaintOption(colorIndex, paintFinish, (numOptions > 1)));
      }

      // TODO: make customer interface with multi and single implementations
      if (numOptions == 1) {
        singleOptionCustomers.add(new Customer(paintOptions));
      } else {
        multipleOptionCustomers.add(new Customer(paintOptions));
      }
    }

    Map<Customer.CustomerType, List<Customer>> customerMap = new HashMap<>();
    customerMap.put(SINGLE_OPTION, singleOptionCustomers);
    customerMap.put(MULTI_OPTION, multipleOptionCustomers);

    return customerMap;
  }

  private void printError(Customer multiOptionCustomer) {
    System.out.println("Impossible to satisfy " + multiOptionCustomer);
  }

  /**
   * Util for printing out PaintOptions
   * @param results
   */
  public static void printPaintOptions(PaintOption[] results) {
    for (PaintOption option : results) {
      System.out.print(option.paintFinish.type + " ");
    }
    System.out.println("\n");
  }

  /**
   * Util for printing the customers.
   * @param customers
   */
  public static void printAllCustomers(Map<Customer.CustomerType, List<Customer>> customers) {
    System.out.println("Customer Choices:");
    for (Customer singleOptionCustomer : customers.get(SINGLE_OPTION)) {
      printCustomerAndChoice(singleOptionCustomer);
    }
    for (Customer multiOptionCustomer : customers.get(MULTI_OPTION)) {
      printCustomerAndChoice(multiOptionCustomer);
    }
  }

  /**
   * Util for printing the customers final PaintOption.
   * @param singleOptionCustomer
   */
  public static void printCustomerAndChoice(Customer singleOptionCustomer) {
    System.out.println(singleOptionCustomer + " CHOICE: " + singleOptionCustomer.getChosenOption());
  }

  public static void main(String[] args) {
    App app = new App(args[0]);
    printPaintOptions(app.mixPaint());
  }
}
