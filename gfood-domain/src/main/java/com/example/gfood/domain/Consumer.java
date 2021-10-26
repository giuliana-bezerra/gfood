package com.example.gfood.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.gfood.common.PersonName;

@Entity
@Table(name = "consumers")
public class Consumer {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private PersonName name;

  private Consumer() {
  }

  public Consumer(PersonName name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public PersonName getName() {
    return name;
  }
}
