package com.example.gfood.consumerservice.api;

public class CreateConsumerResponse {
  private Long id;

  public CreateConsumerResponse() {
  }

  public CreateConsumerResponse(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
