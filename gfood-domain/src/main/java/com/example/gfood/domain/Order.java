package com.example.gfood.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.gfood.common.Money;
import com.example.gfood.common.UnsupportedStateTransitionException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
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

  @Enumerated(EnumType.STRING)
  private OrderState orderState;

  private LocalDateTime readyBy;
  private LocalDateTime acceptTime;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "order_minimum"))
  private Money orderMinimum = new Money("1.0");

  @ManyToOne
  private Courier assignedCourier;

  public Order() {

  }

  public Order(Long consumerId, Restaurant restaurant, List<OrderItem> orderItems) {
    this.consumerId = consumerId;
    this.restaurant = restaurant;
    setOrderItems(orderItems);
    this.orderState = OrderState.APPROVED;
  }

  public Order(Long id, Long consumerId, Restaurant restaurant, List<OrderItem> orderItems) {
    this.id = id;
    this.consumerId = consumerId;
    this.restaurant = restaurant;
    setOrderItems(orderItems);
    this.orderState = OrderState.APPROVED;
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

  public OrderState getOrderState() {
    return orderState;
  }

  public void setOrderState(OrderState orderState) {
    this.orderState = orderState;
  }

  public LocalDateTime getReadyBy() {
    return readyBy;
  }

  public void setReadyBy(LocalDateTime readyBy) {
    this.readyBy = readyBy;
  }

  public LocalDateTime getAcceptTime() {
    return acceptTime;
  }

  public void setAcceptTime(LocalDateTime acceptTime) {
    this.acceptTime = acceptTime;
  }

  public Courier getAssignedCourier() {
    return assignedCourier;
  }

  public void setAssignedCourier(Courier assignedCourier) {
    this.assignedCourier = assignedCourier;
  }

  public void accept(OrderAcceptance orderAcceptance) {
    if (orderState != OrderState.APPROVED)
      throw new UnsupportedStateTransitionException(orderState);

    this.acceptTime = orderAcceptance.getAcceptTime();
    if (!acceptTime.isBefore(orderAcceptance.getReadyBy()))
      throw new IllegalArgumentException("readyBy is not in the future");
    this.readyBy = orderAcceptance.getReadyBy();
    this.orderState = OrderState.ACCEPTED;
  }

  public void cancel() {
    if (this.orderState != OrderState.APPROVED)
      throw new UnsupportedStateTransitionException(this.orderState);
    this.orderState = OrderState.CANCELLED;
  }

  public void revise(OrderRevision orderRevision) {
    if (this.orderState != OrderState.APPROVED)
      throw new UnsupportedStateTransitionException(this.orderState);

    if (!orderRevision.getRevisedItemQuantities().isEmpty())
      updateOrderItems(orderRevision);
  }

  private void updateOrderItems(OrderRevision orderRevision) {
    this.orderItems.stream().forEach(orderItem -> {
      Integer revised = orderRevision.getRevisedItemQuantities().get(orderItem.getMenuItemId());
      orderItem.setQuantity(revised);
      if (!getOrderTotal().isGreaterThanOrEqual(orderMinimum))
        throw new OrderMinimumNotMetException();
    });
  }

  public void schedule(Courier courier) {
    this.assignedCourier = courier;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

}
