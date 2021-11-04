package com.example.gfood.orderservice.web;

import java.util.stream.Collectors;

import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.domain.OrderService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @RequestMapping(path = "/{orderId}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
  public ResponseEntity<GetOrderResponse> get(@PathVariable Long orderId) {
    return orderService.findById(orderId)
        .map(order -> new ResponseEntity<GetOrderResponse>(
            new GetOrderResponse(order.getId(), order.getOrderTotal(), order.getRestaurant().getName()), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<CreateOrderResponse> create(@RequestBody CreateOrderRequest request) {
    return new ResponseEntity<CreateOrderResponse>(
        new CreateOrderResponse(
            orderService.create(request.getConsumerId(), request.getRestaurantId(), request.getOrderItems()).getId()),
        HttpStatus.CREATED);
  }
}
