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

  public PersonName getName() {
    return name;
  }

  public Address getAddress() {
    return address;
  }

  public Boolean getAvailable() {
    return available;
  }

}
