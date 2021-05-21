package com.bjordan.paintshop;

import static com.bjordan.paintshop.App.printAllCustomers;
import static com.bjordan.paintshop.App.printPaintOptions;
import static com.bjordan.paintshop.PaintFinish.GLOSS;
import static com.bjordan.paintshop.PaintFinish.MATTE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppTest {

  /*
   * Two customers with multiple options and one with a single option
   */
  @Test
  public void sampleOne() {
    App app = new App("data/sample-1.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(5, results.length);
    assertEquals(results[0].paintFinish, GLOSS);
    assertEquals(results[1].paintFinish, GLOSS);
    assertEquals(results[2].paintFinish, GLOSS);
    assertEquals(results[3].paintFinish, GLOSS);
    assertEquals(results[4].paintFinish, MATTE);
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());

  }

  /*
   * Several customers with single options
   */
  @Test
  public void sampleTwo() {
    App app = new App("data/sample-2.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 5);
    assertEquals(results[0].paintFinish, GLOSS);
    assertEquals(results[1].paintFinish, MATTE);
    assertEquals(results[2].paintFinish, GLOSS);
    assertEquals(results[3].paintFinish, MATTE);
    assertEquals(results[4].paintFinish, GLOSS);
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * Two customers one with a single option which negates the multi option customer GLOSS option
   */
  @Test
  public void sampleThree() {
    App app = new App("data/sample-3.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 2);
    assertEquals(results[0].paintFinish, MATTE);
    assertEquals(results[1].paintFinish, MATTE);
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * Two customers with single options want the same MATTE option
   */
  @Test
  public void sameSingleOption() {
    App app = new App("data/two-single-options-pass.txt");
    PaintOption[] results = app.mixPaint();
    assertEquals(2, results.length);
    printPaintOptions(results);

    System.out.println("");
    printAllCustomers(app.getCustomers());
  }

  /*
   * No match possible
   */
  @Test
  public void failedMatch() {
    App app = new App("data/no-match.txt");
    app.mixPaint();
  }

  @Test
  public void invalidFinish() {
    App app = new App("data/invalid-finish.txt");
    PaintOption[] paintOptions = app.mixPaint();
    assertEquals(0, paintOptions.length);
  }

  @Test
  public void invalidPaintNumber() {
    App app = new App("data/invalid-paint-number.txt");
    PaintOption[] paintOptions = app.mixPaint();
    assertEquals(0, paintOptions.length);
  }

  @Test
  public void invalidNoCustomers() {
    App app = new App("data/invalid-no-customers.txt");
    PaintOption[] paintOptions = app.mixPaint();
    assertEquals(5, paintOptions.length);
  }

  @Test
  public void invalidNoFile() {
    App app = new App("data/not-a-file.txt");
    PaintOption[] paintOptions = app.mixPaint();
    assertEquals(0, paintOptions.length);
  }
}
