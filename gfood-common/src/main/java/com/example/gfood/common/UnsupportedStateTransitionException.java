package com.example.gfood.common;

public class UnsupportedStateTransitionException extends RuntimeException {
  @SuppressWarnings("rawtypes")
  public UnsupportedStateTransitionException(Enum state) {
    super("current state: " + state);
  }
}
