package com.example.gfood.restaurantservice.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.gfood.common.Address;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateRestaurantRequest {
  @NotNull(message = "Name is required.")
  private String name;
  @NotNull(message = "Menu is required.")
  @Valid
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
