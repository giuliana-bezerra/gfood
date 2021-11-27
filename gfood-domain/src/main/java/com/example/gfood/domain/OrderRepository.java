package com.example.gfood.domain;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface OrderRepository extends CrudRepository<Order, Long>, QueryByExampleExecutor<Order> {
  // @EntityGraph(attributePaths = { "restaurant", "orderItems" }, type =
  // EntityGraphType.FETCH)
  // List<Order> findByConsumerId(Long consumerId);
  @Override
  @EntityGraph(attributePaths = { "restaurant", "orderItems" }, type = EntityGraphType.FETCH)
  <S extends Order> List<S> findAll(Example<S> example);
}
