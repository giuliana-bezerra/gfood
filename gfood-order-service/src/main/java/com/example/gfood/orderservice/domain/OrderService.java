package com.example.gfood.orderservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.example.gfood.common.DateType;
import com.example.gfood.consumerservice.domain.ConsumerService;
import com.example.gfood.courierservice.domain.CourierService;
import com.example.gfood.domain.Action;
import com.example.gfood.domain.Courier;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderAcceptance;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.OrderRepository;
import com.example.gfood.domain.OrderRevision;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantRepository;
import com.example.gfood.orderservice.api.OrderItemDTO;

import org.springframework.data.domain.Example;

public class OrderService {
  private OrderRepository orderRepository;
  private RestaurantRepository restaurantRepository;
  private ConsumerService consumerService;
  private CourierService courierService;
  private DateType dateType;

  public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
      ConsumerService consumerService, CourierService courierService, DateType dateType) {
    this.orderRepository = orderRepository;
    this.restaurantRepository = restaurantRepository;
    this.consumerService = consumerService;
    this.courierService = courierService;
    this.dateType = dateType;
  }

  public Optional<Order> findById(Long orderId) {
    return orderRepository.findById(orderId);
  }

  public List<Order> list(Long consumerId) {
    Order order = new Order();
    order.setConsumerId(consumerId);
    Iterable<Order> ordersIt = orderRepository.findAll(Example.of(order));
    return StreamSupport.stream(ordersIt.spliterator(), false).collect(Collectors.toList());
  }

  @Transactional
  public Order create(Long consumerId, Long restaurantId, List<OrderItemDTO> orderItems) {
    Order order = new Order(consumerId, new Restaurant(restaurantId), makeOrderItems(orderItems, restaurantId));

    consumerService.validateOrderForConsumer(consumerId, order.getOrderTotal());
    orderRepository.save(order);
    return order;
  }

  @Transactional
  public Optional<Order> cancel(Long orderId) {
    return orderRepository.findById(orderId).map(order -> {
      order.cancel();
      return Optional.of(order);
    }).orElseGet(() -> Optional.empty());
  }

  @Transactional
  public Optional<Order> revise(Long orderId, OrderRevision orderRevision) {
    return orderRepository.findById(orderId).map(order -> {
      order.revise(orderRevision);
      return Optional.of(order);
    }).orElseGet(() -> Optional.empty());
  }

  @Transactional
  public Optional<Order> accept(Long orderId, OrderAcceptance orderAcceptance) {
    orderAcceptance.setAcceptTime(dateType.now());
    return orderRepository.findById(orderId).map(order -> {
      order.accept(orderAcceptance);
      scheduleDelivery(order, orderAcceptance.getReadyBy());
      return Optional.of(order);
    }).orElseGet(() -> Optional.empty());
  }

  private void scheduleDelivery(Order order, LocalDateTime readyBy) {
    Courier courier = courierService.findCourierAvailable();

    courier.addAction(Action.makePickup(order, readyBy));
    courier.addAction(Action.makeDropoff(order, readyBy.plusMinutes(30))); // Delivery estimation
    courierService.updateAvailability(courier, false);

    order.schedule(courier);
  }

  private List<OrderItem> makeOrderItems(List<OrderItemDTO> orderItems, Long restaurantId) {
    // Load menu items from restaurant
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

    return orderItems.stream().map(orderItem -> {
      MenuItem menuItem = restaurant.findMenuItem(orderItem.getMenuItemId())
          .orElseThrow(() -> new InvalidMenuItemException(orderItem.getMenuItemId()));
      return new OrderItem(orderItem.getMenuItemId(), menuItem.getName(), menuItem.getPrice(), orderItem.getQuantity());
    }).collect(Collectors.toList());
  }
}
