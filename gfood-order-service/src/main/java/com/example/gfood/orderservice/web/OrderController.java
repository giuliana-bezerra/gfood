package com.example.gfood.orderservice.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.gfood.common.HttpError;
import com.example.gfood.common.UnsupportedStateTransitionException;
import com.example.gfood.domain.OrderAcceptance;
import com.example.gfood.domain.OrderRevision;
import com.example.gfood.orderservice.api.AcceptOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.api.ReviseOrderRequest;
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

  @SuppressWarnings("rawtypes")
  @RequestMapping(path = "/{orderId}/cancel", method = RequestMethod.POST)
  public ResponseEntity cancel(@PathVariable Long orderId) {
    try {
      return orderService.cancel(orderId)
          .map(order -> new ResponseEntity<GetOrderResponse>(new GetOrderResponse(order.getId(), order.getOrderTotal(),
              order.getRestaurant().getName(), order.getOrderState().name()), HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (UnsupportedStateTransitionException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new HttpError(
          "Invalid order state for cancelling - " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }
  }

  @SuppressWarnings("rawtypes")
  @RequestMapping(path = "/{orderId}/revise", method = RequestMethod.POST)
  public ResponseEntity revise(@PathVariable Long orderId, @RequestBody @Valid ReviseOrderRequest reviseOrderRequest) {
    try {
      return orderService.revise(orderId, new OrderRevision(reviseOrderRequest.getRevisedItemQuantities()))
          .map(order -> new ResponseEntity<GetOrderResponse>(new GetOrderResponse(order.getId(), order.getOrderTotal(),
              order.getRestaurant().getName(), order.getOrderState().name()), HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (UnsupportedStateTransitionException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new HttpError(
          "Invalid order state for revising - " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }
  }

  @SuppressWarnings("rawtypes")
  @RequestMapping(path = "/{orderId}/accept", method = RequestMethod.POST)
  public ResponseEntity accept(@PathVariable Long orderId, @RequestBody @Valid AcceptOrderRequest acceptOrderRequest) {
    try {
      return orderService.accept(orderId, new OrderAcceptance(acceptOrderRequest.getReadyBy()))
          .map(order -> new ResponseEntity<GetOrderResponse>(new GetOrderResponse(order.getId(), order.getOrderTotal(),
              order.getRestaurant().getName(), order.getOrderState().name()), HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (UnsupportedStateTransitionException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new HttpError(
          "Invalid order state for accepting - " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new HttpError(
          "Invalid order state for accepting - " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value()));
    }
  }

}
