package com.example.gfood.consumerservice.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.gfood.common.Money;
import com.example.gfood.common.PersonName;
import com.example.gfood.domain.Consumer;
import com.example.gfood.domain.ConsumerRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { ConsumerService.class })
@AutoConfigureMockMvc
public class ConsumerServiceTest {
  @Autowired
  private ConsumerService service;

  @MockBean
  private ConsumerRepository repository;

  @Test
  public void shouldFindConsumer() {
    Consumer consumer = new Consumer(1L, new PersonName("Giuliana", "Bezerra"));
    when(repository.findById(1L)).thenReturn(Optional.of(consumer));
    assertEquals(consumer, service.findById(1L).get());
  }

  @Test
  public void shouldNotFindConsumer() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertFalse(service.findById(1L).isPresent());
  }

  @Test
  public void shouldValidateOrderForConsumer() {
    when(repository.findById(1L)).thenReturn(Optional.of(new Consumer(1L, new PersonName("Giuliana", "Bezerra"))));
    assertDoesNotThrow(() -> service.validateOrderForConsumer(1L, new Money("1.0")));
  }

  @Test
  public void shouldThrowConsumerNotFound() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ConsumerNotFoundException.class, () -> service.validateOrderForConsumer(1L, new Money("1.0")));
  }

  @Test
  public void shouldThrowConsumerOrderInvalid() {
    when(repository.findById(1L)).thenReturn(Optional.of(new Consumer(1L, new PersonName("Giuliana", "Bezerra"))));
    assertThrows(ConsumerOrderInvalidException.class, () -> service.validateOrderForConsumer(1L, new Money("-1.0")));
  }
}
