package com.bjordan.paintshop;

import static com.bjordan.paintshop.App.printAllCustomers;
import static com.bjordan.paintshop.App.printPaintOptions;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class AppTest {

  /*
   * Two customers with multiple options and one with a single option
   */
  @Test
  public void sampleOne() throws IOException {
    App app = new App("data/sample-1.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(5, results.length);
    assertEquals(results[0].paintFinish, "G");
    assertEquals(results[1].paintFinish, "G");
    assertEquals(results[2].paintFinish, "G");
    assertEquals(results[3].paintFinish, "G");
    assertEquals(results[4].paintFinish, "M");
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());

  }

  /*
   * Several customers with single options
   */
  @Test
  public void sampleTwo() throws IOException {
    App app = new App("data/sample-2.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 5);
    assertEquals(results[0].paintFinish, "G");
    assertEquals(results[1].paintFinish, "M");
    assertEquals(results[2].paintFinish, "G");
    assertEquals(results[3].paintFinish, "M");
    assertEquals(results[4].paintFinish, "G");
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * Two customers one with a single option which negates the multi option customer GLOSS option
   */
  @Test
  public void sampleThree() throws IOException {
    App app = new App("data/sample-3.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 2);
    assertEquals(results[0].paintFinish, "M");
    assertEquals(results[1].paintFinish, "M");
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * Two customers with single options want the same MATTE option
   */
  @Test
  public void sameSingleOption() throws IOException {
    App app = new App("data/two-single-options-pass.txt");
    PaintOption[] results = app.mixPaint();
    assertEquals(results.length, 2);
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * No match possible
   */
  @Test
  public void failedMatch() throws IOException {
    App app = new App("data/no-match.txt");
    app.mixPaint();
  }
}
