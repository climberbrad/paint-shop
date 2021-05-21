package com.bjordan.paintshop;

import java.util.List;

public class Customer {
  public final List<PaintOption> paintOptions;
  private PaintOption chosenOption;

  public Customer(List<PaintOption> paintOptions) {
    this.paintOptions = paintOptions;
  }

  public PaintOption getChosenOption() {
    return chosenOption;
  }

  public void setChosenOption(PaintOption chosenOption) {
    this.chosenOption = chosenOption;
  }

  public enum CustomerType {
    SINGLE_OPTION,
    MULTI_OPTION;
  }

  @Override
  public String toString() {
    return "Customer: " + paintOptions;
  }
}
