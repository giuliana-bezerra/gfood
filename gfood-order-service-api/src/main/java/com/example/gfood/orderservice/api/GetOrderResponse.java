package com.example.gfood.orderservice.api;

import com.example.gfood.common.Money;

public class GetOrderResponse {
  private Long id;
  private Money orderTotal;
  private String restaurantName;

  public GetOrderResponse() {

  }

  public GetOrderResponse(Long id, Money orderTotal, String restaurantName) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.restaurantName = restaurantName;
  }

  public Long getId() {
    return id;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

}
