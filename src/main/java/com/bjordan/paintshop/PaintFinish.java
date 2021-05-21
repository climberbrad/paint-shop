package com.bjordan.paintshop;

public enum PaintFinish {
  GLOSS("G"),
  MATTE("M");

  public final String type;

  PaintFinish(String type) {
    this.type = type;
  }

  public static PaintFinish fromString(String finishLetter) {
    for (PaintFinish finish : PaintFinish.values()) {
      if (finish.type.equalsIgnoreCase(finishLetter)) {
        return finish;
      }
    }
    return null;
  }
}
