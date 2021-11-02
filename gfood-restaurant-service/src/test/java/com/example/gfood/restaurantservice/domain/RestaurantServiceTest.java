package com.example.gfood.restaurantservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantRepository;
import com.example.gfood.restaurantservice.api.CreateRestaurantRequest;
import com.example.gfood.restaurantservice.api.MenuItemDTO;
import com.example.gfood.restaurantservice.api.RestaurantMenuDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { RestaurantService.class })
@AutoConfigureMockMvc
public class RestaurantServiceTest {
  @Autowired
  private RestaurantService service;

  @MockBean
  private RestaurantRepository repository;

  @Test
  public void shouldFindRestaurant() {
    Restaurant restaurant = new Restaurant(1L, "Restaurant", new Address());
    when(repository.findById(1L)).thenReturn(Optional.of(restaurant));
    assertEquals(restaurant, service.findById(1L).get());
  }

  @Test
  public void shouldNotFindRestaurant() {
    when(repository.findById(1L)).thenReturn(Optional.empty());
    assertFalse(service.findById(1L).isPresent());
  }

  @Test
  public void shouldCreateRestaurant() {
    List<MenuItemDTO> menuItems = new ArrayList<>() {
      {
        add(new MenuItemDTO("1", "Feijão Tropeiro", new Money("50.20")));
      }
    };
    String name = "Restaurant";
    Address address = new Address("Street 1", "Street 2", "City", "State", "Zip");
    RestaurantMenuDTO menu = new RestaurantMenuDTO(menuItems);
    CreateRestaurantRequest request = new CreateRestaurantRequest(name, address, menu);

    assertEquals(name, service.create(request).getName());
    assertEquals(address, service.create(request).getAddress());
    assertEquals(new ArrayList<>() {
      {
        add(new MenuItem("1", "Feijão Tropeiro", new Money("50.20")));
      }
    }, service.create(request).getMenuItens());
  }
}
