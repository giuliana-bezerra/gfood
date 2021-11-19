package com.example.gfood.domain;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
  @EntityGraph(attributePaths = { "restaurant", "orderItems" }, type = EntityGraphType.FETCH)
  List<Order> findByConsumerId(Long consumerId);
}
