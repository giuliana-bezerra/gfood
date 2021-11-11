package com.example.gfood.consumerservice.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.gfood.common.PersonName;

public class CreateConsumerRequest {
  @NotNull(message = "Name is required.")
  @Valid
  private PersonName name;

  public CreateConsumerRequest() {

  }

  public CreateConsumerRequest(PersonName name) {
    this.name = name;
  }

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }
}
