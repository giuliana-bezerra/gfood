package com.example.gfood.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "couriers")
@DynamicUpdate
public class Courier {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  private PersonName name;

  @Embedded
  private Address address;

  private Boolean available;

  public Courier() {
  }

  public Courier(PersonName name, Address address) {
    this.name = name;
    this.address = address;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PersonName getName() {
    return name;
  }

  public void setName(PersonName name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }
}
