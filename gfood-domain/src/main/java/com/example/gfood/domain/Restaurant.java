package com.example.gfood.domain;

import java.util.List;
import java.util.Optional;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.gfood.common.Address;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "restaurants")
@DynamicUpdate
public class Restaurant {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Embedded
  private Address address;

  @Embedded
  @ElementCollection
  @CollectionTable(name = "restaurant_menu_items")
  private List<MenuItem> menuItens;

  public Restaurant() {
  }

  public Restaurant(String name, Address address, RestaurantMenu menu) {
    this.name = name;
    this.address = address;
    this.menuItens = menu.getMenuItens();
  }

  public Restaurant(Long id, String name, Address address, RestaurantMenu menu) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.menuItens = menu.getMenuItens();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<MenuItem> getMenuItens() {
    return menuItens;
  }

  public void setMenuItens(List<MenuItem> menuItens) {
    this.menuItens = menuItens;
  }

  public Optional<MenuItem> findMenuItem(String menuItemId) {
    return menuItens.stream().filter(menuItem -> menuItem.getId().equals(menuItemId)).findFirst();
  }
}
