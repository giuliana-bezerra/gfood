package com.example.gfood.courierservice.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;

public class CreateCourierRequest {
  @NotNull(message = "Name is required.")
  @Valid
  private PersonName name;
  private Address address;

  public CreateCourierRequest() {
  }

  public CreateCourierRequest(PersonName name, Address address) {
    this.name = name;
    this.address = address;
  }

  public PersonName getName() {
    return name;
  }

  public Address getAddress() {
    return address;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public void setAddress(Address address) {
    this.address = address;
  }
}
