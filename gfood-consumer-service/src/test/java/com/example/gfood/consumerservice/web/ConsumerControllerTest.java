package com.example.gfood.consumerservice.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.example.gfood.common.PersonName;
import com.example.gfood.consumerservice.api.CreateConsumerRequest;
import com.example.gfood.consumerservice.api.CreateConsumerResponse;
import com.example.gfood.consumerservice.api.GetConsumerResponse;
import com.example.gfood.consumerservice.domain.ConsumerService;
import com.example.gfood.consumerservice.main.ConsumerServiceConfig;
import com.example.gfood.domain.Consumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = { ConsumerServiceConfig.class })
@WebMvcTest(ConsumerController.class)
@Import(ConsumerController.class)
public class ConsumerControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ConsumerService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldFindConsumerById() throws Exception {
    Consumer consumer = new Consumer(1L, new PersonName("Mônica", "Geller"));
    GetConsumerResponse response = new GetConsumerResponse(consumer.getId(), consumer.getName());
    String urlTemplate = "/consumers/" + consumer.getId();
    when(service.findById(consumer.getId())).thenReturn(Optional.of(consumer));

    this.mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotFindConsumerById() throws Exception {
    this.mockMvc.perform(get("/consumers/1")).andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  public void shouldCreateConsumer() throws Exception {
    Consumer consumer = new Consumer(1L, new PersonName("Mônica", "Geller"));
    CreateConsumerRequest request = new CreateConsumerRequest(consumer.getName());
    CreateConsumerResponse response = new CreateConsumerResponse(consumer.getId());

    when(service.create(request.getName())).thenReturn(consumer);

    mockMvc
        .perform(post("/consumers").content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotCreateInvalidConsumer() throws Exception {
    mockMvc.perform(post("/consumers").content(objectMapper.writeValueAsString(new CreateConsumerRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(
        post("/consumers").content(objectMapper.writeValueAsString(new CreateConsumerRequest(new PersonName())))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isBadRequest());
  }
}
