package com.example.gfood.courierservice.domain;

import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;
import com.example.gfood.domain.Courier;
import com.example.gfood.domain.CourierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CourierService {
  @Autowired
  private CourierRepository repository;

  public Optional<Courier> findById(Long courierId) {
    return repository.findById(courierId);
  }

  @Transactional
  public Courier create(PersonName name, Address address) {
    Courier courier = new Courier(name, address);
    repository.save(courier);
    return courier;
  }
}
