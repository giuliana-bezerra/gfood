package com.example.gfood.courierservice.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;
import com.example.gfood.courierservice.api.CreateCourierRequest;
import com.example.gfood.courierservice.api.CreateCourierResponse;
import com.example.gfood.courierservice.domain.CourierService;
import com.example.gfood.courierservice.main.CourierServiceConfig;
import com.example.gfood.domain.Courier;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = { CourierServiceConfig.class })
@WebMvcTest(CourierController.class)
@Import(CourierController.class)
public class CourierControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CourierService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldFindCourierById() throws Exception {
    Courier courier = new Courier(1L, new PersonName("firstName", "lastName"),
        new Address("street1", "street2", "city", "state", "zip"));
    String urlTemplate = "/couriers/" + courier.getId();

    when(service.findById(courier.getId())).thenReturn(Optional.of(courier));

    mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(courier)));
  }

  @Test
  public void shouldNotFindCourierById() throws Exception {
    this.mockMvc.perform(get("/couriers/1")).andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  public void shouldCreateCourier() throws Exception {
    Courier courier = new Courier(1L, new PersonName("firstName", "lastName"),
        new Address("street1", "street2", "city", "state", "zip"));
    CreateCourierRequest request = new CreateCourierRequest(courier.getName(), courier.getAddress());
    CreateCourierResponse response = new CreateCourierResponse(courier.getId());

    when(service.create(request.getName(), request.getAddress())).thenReturn(courier);

    mockMvc
        .perform(
            post("/couriers").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotCreateInvalidCourier() throws Exception {
    mockMvc.perform(post("/couriers").content(objectMapper.writeValueAsString(new CreateCourierRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/couriers")
        .content(objectMapper.writeValueAsString(new CreateCourierRequest(new PersonName(null, null), new Address())))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }
}
