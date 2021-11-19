package com.example.gfood.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.gfood.common.Money;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "orders")
@DynamicUpdate
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long consumerId;

  @ManyToOne(fetch = FetchType.LAZY)
  private Restaurant restaurant;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems;

  public Order() {

  }

  public Order(Long consumerId, Restaurant restaurant, List<OrderItem> orderItems) {
    this.consumerId = consumerId;
    this.restaurant = restaurant;
    setOrderItems(orderItems);
  }

  public Order(Long id, Long consumerId, Restaurant restaurant, List<OrderItem> orderItems) {
    this.id = id;
    this.consumerId = consumerId;
    this.restaurant = restaurant;
    setOrderItems(orderItems);
  }

  public Money getOrderTotal() {
    return orderItems.stream().map(OrderItem::getTotal).reduce(Money.ZERO, Money::add);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getConsumerId() {
    return consumerId;
  }

  public void setConsumerId(Long consumerId) {
    this.consumerId = consumerId;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
    this.orderItems.stream().forEach(orderItem -> orderItem.setOrder(this));
  }
}
