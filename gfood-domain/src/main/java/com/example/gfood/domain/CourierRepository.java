package com.example.gfood.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface CourierRepository extends CrudRepository<Courier, Long>, QueryByExampleExecutor<Courier> {
  List<Courier> findByAvailableTrue();

}
