package com.bjordan.paintshop;

import static com.bjordan.paintshop.App.printPaintOptions;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class AppTest {

  @Test
  public void sampleOne() throws IOException {
    App app = new App("data/sample-1.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 5);
    assertEquals(results[0].paintFinish, "G");
    assertEquals(results[1].paintFinish, "G");
    assertEquals(results[2].paintFinish, "G");
    assertEquals(results[3].paintFinish, "G");
    assertEquals(results[4].paintFinish, "M");
    printPaintOptions(results);
  }

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
  }

  @Test
  public void sampleThree() throws IOException {
    App app = new App("data/sample-3.txt");
    PaintOption[] results = app.mixPaint();

    assertEquals(results.length, 2);
    assertEquals(results[0].paintFinish, "M");
    assertEquals(results[1].paintFinish, "M");
    printPaintOptions(results);
  }

//  @Test
//  public void failedMatch() throws IOException {
//    App app = new App("data/no-match.txt");
//    app.mixPaint();
//  }
}
