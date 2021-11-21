package com.example.gfood.orderservice.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.domain.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @RequestMapping(path = "/{orderId}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
  public ResponseEntity<GetOrderResponse> get(@PathVariable Long orderId) {
    return orderService.findById(orderId)
        .map(order -> new ResponseEntity<GetOrderResponse>(new GetOrderResponse(order.getId(), order.getOrderTotal(),
            order.getRestaurant().getName(), order.getOrderState().name()), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<GetOrderResponse>> list(@RequestParam Long consumerId) {
    List<GetOrderResponse> response = orderService.list(consumerId).stream()
        .map(order -> new GetOrderResponse(order.getId(), order.getOrderTotal(), order.getRestaurant().getName(),
            order.getOrderState().name()))
        .collect(Collectors.toList());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<CreateOrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
    return new ResponseEntity<CreateOrderResponse>(
        new CreateOrderResponse(
            orderService.create(request.getConsumerId(), request.getRestaurantId(), request.getOrderItems()).getId()),
        HttpStatus.CREATED);
  }

  @RequestMapping(path = "/{orderId}/cancel", method = RequestMethod.POST)
  public ResponseEntity<GetOrderResponse> cancel(@PathVariable Long orderId) {
    return orderService.cancel(orderId)
        .map(order -> new ResponseEntity<GetOrderResponse>(new GetOrderResponse(order.getId(), order.getOrderTotal(),
            order.getRestaurant().getName(), order.getOrderState().name()), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

  }
}
