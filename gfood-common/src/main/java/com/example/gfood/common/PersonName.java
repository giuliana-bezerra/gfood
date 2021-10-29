package com.example.gfood.common;

import javax.persistence.Embeddable;

@Embeddable
public class PersonName {
  private String firstName;
  private String lastName;

  public PersonName() {

  }

  public PersonName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
