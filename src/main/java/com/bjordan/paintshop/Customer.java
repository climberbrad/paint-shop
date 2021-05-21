package com.bjordan.paintshop;

import java.util.List;

public class Customer {
  public final List<PaintOption> paintOptions;

  public Customer(List<PaintOption> paintOptions) {
    this.paintOptions = paintOptions;
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
