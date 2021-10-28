package com.example.gfood.consumerservice.api;

import com.example.gfood.common.PersonName;

public class CreateConsumerRequest {
  private PersonName name;

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public CreateConsumerRequest(PersonName name) {

    this.name = name;
  }
}
