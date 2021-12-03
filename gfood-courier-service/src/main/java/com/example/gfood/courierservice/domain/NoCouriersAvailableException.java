package com.example.gfood.courierservice.domain;

public class NoCouriersAvailableException extends RuntimeException {
  public NoCouriersAvailableException() {
    super("No couriers available for delivering the order");
  }
}