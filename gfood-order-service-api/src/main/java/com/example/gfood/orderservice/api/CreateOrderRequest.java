package com.example.gfood.orderservice.api;

import java.util.List;

public class CreateOrderRequest {
  private Long restaurantId;
  private Long consumerId;
  private List<OrderItemDTO> orderItems;

  public CreateOrderRequest() {

  }

  public CreateOrderRequest(Long restaurantId, Long consumerId, List<OrderItemDTO> orderItems) {
    this.restaurantId = restaurantId;
    this.consumerId = consumerId;
    this.orderItems = orderItems;
  }

  public Long getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(Long restaurantId) {
    this.restaurantId = restaurantId;
  }

  public Long getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(Long consumerId) {
    this.consumerId = consumerId;
  }

  public List<OrderItemDTO> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItemDTO> orderItems) {
    this.orderItems = orderItems;
  }

}
