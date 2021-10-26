package com.example.gfood.consumerservice.domain;

import java.util.Optional;

import com.example.gfood.common.PersonName;
import com.example.gfood.domain.Consumer;
import com.example.gfood.domain.ConsumerRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class ConsumerService {
  @Autowired
  private ConsumerRepository consumerRepository;

  public Optional<Consumer> findById(long consumerId) {
    return consumerRepository.findById(consumerId);
  }

  public Consumer create(PersonName name) {
    Consumer consumer = consumerRepository.save(new Consumer(name));
    return consumer;
  }
}
