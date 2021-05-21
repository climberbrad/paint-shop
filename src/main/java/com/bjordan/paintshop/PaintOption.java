package com.bjordan.paintshop;

public class PaintOption {
  public final int colorIndex;
  public final String paintFinish;
  public final boolean mutable;

  public PaintOption(int colorIndex, String paintFinish, boolean mutable) {
    this.colorIndex = colorIndex;
    this.paintFinish = paintFinish;
    this.mutable = mutable;
  }

  @Override
  public String toString() {
    return colorIndex + " " + paintFinish;
  }
}
