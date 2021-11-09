package com.example.gfood.orderservice.api;

public class CreateOrderResponse {
  private Long id;

  public CreateOrderResponse() {
  }

  public CreateOrderResponse(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
