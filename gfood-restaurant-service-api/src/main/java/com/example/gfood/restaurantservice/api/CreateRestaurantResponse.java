package com.example.gfood.restaurantservice.api;

public class CreateRestaurantResponse {
  private Long id;

  public CreateRestaurantResponse() {

  }

  public CreateRestaurantResponse(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
