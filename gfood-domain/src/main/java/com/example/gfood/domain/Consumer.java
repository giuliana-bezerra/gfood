package com.example.gfood.domain;

import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.gfood.common.Money;
import com.example.gfood.common.PersonName;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "consumers")
@DynamicUpdate
public class Consumer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private PersonName name;

  public Consumer() {

  }

  public Consumer(PersonName name) {
    this.name = name;
  }

  public Consumer(Long id, PersonName name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public PersonName getName() {
    return name;
  }

  public Optional<Boolean> validateOrderByConsumer(Money orderTotal) {
    if (orderTotal.isGreaterThanOrEqual(Money.ZERO))
      return Optional.of(true);
    else
      return Optional.empty();
  }
}
