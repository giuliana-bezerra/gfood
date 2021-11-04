package com.example.gfood.orderservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.example.gfood.consumerservice.domain.ConsumerService;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.OrderRepository;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantRepository;
import com.example.gfood.orderservice.api.OrderItemDTO;

public class OrderService {
  private OrderRepository orderRepository;
  private RestaurantRepository restaurantRepository;
  private ConsumerService consumerService;

  public OrderService(OrderRepository orderRepository, RestaurantRepository restaurantRepository,
      ConsumerService consumerService) {
    this.orderRepository = orderRepository;
    this.restaurantRepository = restaurantRepository;
    this.consumerService = consumerService;
  }

  public Optional<Order> findById(Long consumerId) {
    return orderRepository.findById(consumerId);
  }

  @Transactional
  public Order create(Long consumerId, Long restaurantId, List<OrderItemDTO> orderItems) {
    Order order = new Order(consumerId, new Restaurant(restaurantId), makeOrderItems(orderItems, restaurantId));
    consumerService.validateOrderForConsumer(consumerId, order.getOrderTotal());
    orderRepository.save(order);
    return order;
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
