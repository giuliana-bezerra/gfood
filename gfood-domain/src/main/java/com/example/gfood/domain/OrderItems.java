package com.example.gfood.domain;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;

import com.example.gfood.common.Money;

@Embeddable
public class OrderItems {
  @ElementCollection
  @CollectionTable(name = "order_items")
  private List<OrderItem> orderItems;

  public OrderItems() {

  }

  public OrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public Money orderTotal() {
    return orderItems.stream().map(OrderItem::getTotal).reduce(Money.ZERO, Money::add);
  }
}
