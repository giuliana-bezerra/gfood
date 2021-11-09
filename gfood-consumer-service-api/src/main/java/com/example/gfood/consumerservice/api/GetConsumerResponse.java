package com.example.gfood.consumerservice.api;

import com.example.gfood.common.PersonName;

public class GetConsumerResponse {
  private Long id;
  private PersonName name;

  public GetConsumerResponse(Long id, PersonName name) {
    this.id = id;
    this.name = name;
  }

  public PersonName getName() {
    return name;
  }

  public Long getId() {
    return this.id;
  }

}
