package com.example.gfood.courierservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.PersonName;
import com.example.gfood.domain.Courier;
import com.example.gfood.domain.CourierRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { CourierService.class })
@AutoConfigureMockMvc
public class CourierServiceTest {
  @Autowired
  private CourierService service;

  @MockBean
  private CourierRepository repository;

  @Test
  public void shouldFindCourier() {
    Courier courier = new Courier(new PersonName("firstName", "lastName"),
        new Address("street1", "street2", "city", "state", "zip"));
    when(repository.findById(1L)).thenReturn(Optional.of(courier));
    assertEquals(courier, service.findById(1L).get());
  }

  @Test
  public void shouldNotFindCourier() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertFalse(service.findById(1L).isPresent());
  }

  @Test
  public void shouldCreateCourier() {
    PersonName name = new PersonName("firstName", "lastName");
    Address address = new Address("street1", "street2", "city", "state", "zip");
    Courier courier = service.create(name, address);
    assertEquals(courier.getName(), name);
    assertEquals(courier.getAddress(), address);
  }
}
