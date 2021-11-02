package com.example.gfood.consumerservice.web;

import com.example.gfood.consumerservice.api.CreateConsumerRequest;
import com.example.gfood.consumerservice.api.CreateConsumerResponse;
import com.example.gfood.consumerservice.api.GetConsumerResponse;
import com.example.gfood.consumerservice.domain.ConsumerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/consumers")
public class ConsumerController {
  @Autowired
  private ConsumerService consumerService;

  @RequestMapping(method = RequestMethod.GET, path = "/{consumerId}", produces = { "application/json; charset=UTF-8" })
  public ResponseEntity<GetConsumerResponse> get(@PathVariable Long consumerId) {
    return consumerService.findById(consumerId).map(
        consumer -> new ResponseEntity<>(new GetConsumerResponse(consumer.getId(), consumer.getName()), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<CreateConsumerResponse> create(@RequestBody CreateConsumerRequest request) {
    return new ResponseEntity<>(new CreateConsumerResponse(consumerService.create(request.getName()).getId()),
        HttpStatus.CREATED);
  }
}
