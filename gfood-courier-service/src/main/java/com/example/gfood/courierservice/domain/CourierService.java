package com.example.gfood.courierservice.domain;

import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;
import com.example.gfood.courierservice.api.UpdateCourierRequest;
import com.example.gfood.domain.Courier;
import com.example.gfood.domain.CourierRepository;

import org.springframework.transaction.annotation.Transactional;

public class CourierService {
  private CourierRepository repository;

  public CourierService(CourierRepository repository) {
    this.repository = repository;
  }

  public Optional<Courier> findById(Long courierId) {
    return repository.findById(courierId);
  }

  public Courier findCourierAvailable() {
    try {
      List<Courier> couriers = repository.findByAvailableTrue();
      return couriers.get(0);
    } catch (IndexOutOfBoundsException e) {
      throw new NoCouriersAvailableException();
    }
  }

  @Transactional
  public Courier create(PersonName name, Address address) {
    Courier courier = new Courier(name, address);
    repository.save(courier);
    return courier;
  }

  @Transactional
  public Optional<Courier> update(Long courierId, UpdateCourierRequest request) {
    Optional<Courier> courierOpt = repository.findById(courierId);
    courierOpt.ifPresent(courier -> {
      courier.setAvailable(request.isAvailable());
    });
    return courierOpt;
  }

  public void updateAvailability(Courier courier, boolean available) {
    courier.setAvailable(available);
  }

}
