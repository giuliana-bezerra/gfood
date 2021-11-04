package com.example.gfood.orderservice.domain;

public class InvalidMenuItemException extends RuntimeException {
  public InvalidMenuItemException(String menuItemId) {
    super("Invalid menu item id " + menuItemId);
  }
}
