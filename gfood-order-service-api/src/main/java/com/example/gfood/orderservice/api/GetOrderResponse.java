package com.example.gfood.orderservice.api;

import java.util.List;

import com.example.gfood.common.Money;
import com.example.gfood.domain.Action;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GetOrderResponse {
  private Long id;
  private Money orderTotal;
  private String restaurantName;
  private String state;
  private Long assignedCourier;
  private List<Action> courierActions;

  public GetOrderResponse() {

  }

  public GetOrderResponse(Long id, Money orderTotal, String restaurantName, String state, Long assignedCourier,
      List<Action> courierActions) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.restaurantName = restaurantName;
    this.state = state;
    this.assignedCourier = assignedCourier;
    this.courierActions = courierActions;
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

  public Long getAssignedCourier() {
    return assignedCourier;
  }

  public void setAssignedCourier(Long assignedCourier) {
    this.assignedCourier = assignedCourier;
  }

  public List<Action> getCourierActions() {
    return courierActions;
  }

  public void setCourierActions(List<Action> courierActions) {
    this.courierActions = courierActions;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
