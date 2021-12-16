package com.example.gfood.orderservice.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateOrderRequest {
  @NotNull(message = "restaurantId is required.")
  @Positive(message = "restaurantId is required.")
  private Long restaurantId;
  @NotNull(message = "consumerId is required.")
  @Positive(message = "consumerId is required.")
  private Long consumerId;
  @NotEmpty(message = "orderItems are required.")
  private List<@Valid OrderItemDTO> orderItems;

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
