package com.example.gfood.domain;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderRevision {
  private Map<String, Integer> revisedItemQuantities;

  public OrderRevision(Map<String, Integer> revisedItemQuantities) {
    this.revisedItemQuantities = revisedItemQuantities;
  }

  public Map<String, Integer> getRevisedItemQuantities() {
    return revisedItemQuantities;
  }

  public void setRevisedItemQuantities(Map<String, Integer> revisedItemQuantities) {
    this.revisedItemQuantities = revisedItemQuantities;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
