package com.example.gfood.orderservice.api;

import com.example.gfood.common.Money;

public class GetOrderResponse {
  private Long id;
  private Money orderTotal;
  private String restaurantName;
  private String state;

  public GetOrderResponse() {

  }

  public GetOrderResponse(Long id, Money orderTotal, String restaurantName, String state) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.restaurantName = restaurantName;
    this.state = state;
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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}
