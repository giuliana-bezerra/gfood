package com.example.gfood.orderservice.api;

public class OrderItemDTO {
  private String menuItemId;
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

}
