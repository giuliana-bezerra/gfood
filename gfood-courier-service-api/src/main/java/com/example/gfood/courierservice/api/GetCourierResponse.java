package com.example.gfood.courierservice.api;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;

public class GetCourierResponse {
  private Long id;
  private PersonName name;
  private Address address;
  private Boolean available;

  public GetCourierResponse(Long id, PersonName name, Address address, Boolean available) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.available = available;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

}
