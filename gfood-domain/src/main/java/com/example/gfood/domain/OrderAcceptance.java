package com.example.gfood.domain;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderAcceptance {
  private LocalDateTime readyBy;
  private LocalDateTime acceptTime;

  public OrderAcceptance() {
  }

  public OrderAcceptance(LocalDateTime readyBy) {
    this.readyBy = readyBy;
  }

  public OrderAcceptance(LocalDateTime readyBy, LocalDateTime acceptTime) {
    this.readyBy = readyBy;
    this.acceptTime = acceptTime;
  }

  public LocalDateTime getReadyBy() {
    return readyBy;
  }

  public void setReadyBy(LocalDateTime readyBy) {
    this.readyBy = readyBy;
  }

  public LocalDateTime getAcceptTime() {
    return acceptTime;
  }

  public void setAcceptTime(LocalDateTime acceptTime) {
    this.acceptTime = acceptTime;
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
