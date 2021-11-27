package com.example.gfood.orderservice.api;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

public class AcceptOrderRequest {
  @NotNull(message = "readyBy is required.")
  private LocalDateTime readyBy;

  public AcceptOrderRequest() {
  }

  public AcceptOrderRequest(LocalDateTime readyBy) {
    this.readyBy = readyBy;
  }

  public LocalDateTime getReadyBy() {
    return readyBy;
  }

  public void setReadyBy(LocalDateTime readyBy) {
    this.readyBy = readyBy;
  }
}
