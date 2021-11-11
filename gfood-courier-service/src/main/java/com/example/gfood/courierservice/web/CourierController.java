package com.example.gfood.courierservice.web;

import com.example.gfood.courierservice.api.CreateCourierRequest;
import com.example.gfood.courierservice.api.CreateCourierResponse;
import com.example.gfood.courierservice.api.GetCourierResponse;
import com.example.gfood.courierservice.domain.CourierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/couriers")
public class CourierController {
  @Autowired
  private CourierService courierService;

  @RequestMapping(method = RequestMethod.GET, path = "/{courierId}", produces = { "application/json; charset=UTF-8" })
  public ResponseEntity<GetCourierResponse> get(@PathVariable Long courierId) {
    return courierService.findById(courierId)
        .map(courier -> new ResponseEntity<>(
            new GetCourierResponse(courier.getId(), courier.getName(), courier.getAddress(), courier.isAvailable()),
            HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<CreateCourierResponse> create(@RequestBody CreateCourierRequest request) {
    return new ResponseEntity<>(
        new CreateCourierResponse(courierService.create(request.getName(), request.getAddress()).getId()),
        HttpStatus.CREATED);
  }
}
