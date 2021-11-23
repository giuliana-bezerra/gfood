package com.example.gfood.orderservice.api;

import java.util.Map;

import javax.validation.constraints.NotEmpty;

public class ReviseOrderRequest {
  @NotEmpty(message = "Revised items are required.")
  private Map<String, Integer> revisedItemQuantities;

  public ReviseOrderRequest() {
  }

  public ReviseOrderRequest(Map<String, Integer> revisedItemQuantities) {
    this.revisedItemQuantities = revisedItemQuantities;
  }

  public Map<String, Integer> getRevisedLineItemQuantities() {
    return revisedItemQuantities;
  }

  public void setRevisedLineItemQuantities(Map<String, Integer> revisedItemQuantities) {
    this.revisedItemQuantities = revisedItemQuantities;
  }
}
