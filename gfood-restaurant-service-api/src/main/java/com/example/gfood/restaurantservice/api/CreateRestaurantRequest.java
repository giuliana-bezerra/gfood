package com.example.gfood.restaurantservice.api;

import com.example.gfood.common.Address;

public class CreateRestaurantRequest {
  private String name;
  private RestaurantMenuDTO menu;
  private Address address;

  public CreateRestaurantRequest(String name, Address address, RestaurantMenuDTO menu) {
    this.name = name;
    this.address = address;
    this.menu = menu;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public RestaurantMenuDTO getMenu() {
    return menu;
  }

  public void setMenu(RestaurantMenuDTO menu) {
    this.menu = menu;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
