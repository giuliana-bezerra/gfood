package com.example.gfood.courierservice.api;

public class CreateCourierResponse {
  private Long id;

  public CreateCourierResponse() {
  }

  public CreateCourierResponse(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
