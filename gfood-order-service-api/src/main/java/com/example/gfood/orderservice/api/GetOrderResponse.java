package com.example.gfood.orderservice.api;

import com.example.gfood.common.Money;

public class GetOrderResponse {
  private Long orderId;
  private Money orderTotal;
  private String restaurantName;

  public GetOrderResponse() {

  }

  public GetOrderResponse(Long orderId, Money orderTotal, String restaurantName) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.restaurantName = restaurantName;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Money getOrderTotal() {
    return orderTotal;
  }

  public void setOrderTotal(Money orderTotal) {
    this.orderTotal = orderTotal;
  }

  public String getRestaurantName() {
    return restaurantName;
  }

  public void setRestaurantName(String restaurantName) {
    this.restaurantName = restaurantName;
  }

}
