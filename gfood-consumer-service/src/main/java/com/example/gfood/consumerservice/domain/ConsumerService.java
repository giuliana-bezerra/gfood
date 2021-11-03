package com.example.gfood.consumerservice.domain;

import java.util.Optional;

import com.example.gfood.common.Money;
import com.example.gfood.common.PersonName;
import com.example.gfood.domain.Consumer;
import com.example.gfood.domain.ConsumerRepository;

import org.springframework.transaction.annotation.Transactional;

public class ConsumerService {
  private ConsumerRepository consumerRepository;

  public ConsumerService(ConsumerRepository consumerRepository) {
    this.consumerRepository = consumerRepository;
  }

  public Optional<Consumer> findById(Long consumerId) {
    return consumerRepository.findById(consumerId);
  }

  @Transactional
  public Consumer create(PersonName name) {
    Consumer consumer = consumerRepository.save(new Consumer(name));
    return consumer;
  }

  public void validateOrderForConsumer(long consumerId, Money orderTotal) {
    Optional<Consumer> consumer = consumerRepository.findById(consumerId);
    consumer.orElseThrow(ConsumerNotFoundException::new).validateOrderByConsumer(orderTotal)
        .orElseThrow(ConsumerOrderInvalidException::new);
  }
}
