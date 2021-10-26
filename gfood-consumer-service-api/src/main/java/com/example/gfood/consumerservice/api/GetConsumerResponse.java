package com.example.gfood.consumerservice.api;

import com.example.gfood.common.PersonName;

public class GetConsumerResponse extends CreateConsumerResponse {
  private PersonName name;

  public PersonName getName() {
    return name;
  }

  public GetConsumerResponse(PersonName name) {
    this.name = name;
  }
}
