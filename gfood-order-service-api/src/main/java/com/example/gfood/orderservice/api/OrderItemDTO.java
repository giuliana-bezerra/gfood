package com.example.gfood.orderservice.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderItemDTO {
  @NotBlank(message = "menuItemId is required.")
  private String menuItemId;
  @NotNull(message = "quantity is required.")
  @Positive(message = "quantity is required.")
  private Integer quantity;

  public OrderItemDTO() {

  }

  public OrderItemDTO(String menuItemId, Integer quantity) {
    this.menuItemId = menuItemId;
    this.quantity = quantity;
  }

  public String getMenuItemId() {
    return menuItemId;
  }

  public void setMenuItemId(String menuItemId) {
    this.menuItemId = menuItemId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
