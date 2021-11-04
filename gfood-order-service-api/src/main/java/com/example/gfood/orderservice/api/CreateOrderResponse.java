package com.example.gfood.orderservice.api;

public class CreateOrderResponse {
  private Long orderId;

  public CreateOrderResponse() {

  }

  public CreateOrderResponse(Long orderId) {
    this.orderId = orderId;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

}
