package com.example.gfood.domain;

import java.time.LocalDateTime;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Embeddable
public class Action {
  @Enumerated(EnumType.STRING)
  private ActionType type;
  private LocalDateTime time;

  @ManyToOne
  private Order order;

  public Action() {
  }

  public Action(ActionType type, Order order, LocalDateTime time) {
    this.type = type;
    this.order = order;
    this.time = time;
  }

  public boolean isActionForOrder(Order order) {
    return this.order.getId().equals(order.getId());
  }

  public static Action makePickup(Order order, LocalDateTime pickupTime) {
    return new Action(ActionType.PICKUP, order, pickupTime);
  }

  public static Action makeDropoff(Order order, LocalDateTime deliveryTime) {
    return new Action(ActionType.DROPOFF, order, deliveryTime);
  }

  public ActionType getType() {
    return type;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

}