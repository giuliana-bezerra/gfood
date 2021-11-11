package com.example.gfood.restaurantservice.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.common.MoneyModuleConfig.MoneyModule;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;
import com.example.gfood.restaurantservice.api.CreateRestaurantRequest;
import com.example.gfood.restaurantservice.api.CreateRestaurantResponse;
import com.example.gfood.restaurantservice.api.GetRestaurantResponse;
import com.example.gfood.restaurantservice.api.MenuItemDTO;
import com.example.gfood.restaurantservice.api.RestaurantMenuDTO;
import com.example.gfood.restaurantservice.domain.RestaurantService;
import com.example.gfood.restaurantservice.main.RestaurantServiceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = { RestaurantServiceConfig.class })
@WebMvcTest(RestaurantController.class)
@Import(value = { RestaurantController.class, MoneyModule.class })
public class RestaurantControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RestaurantService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldFindRestaurantById() throws Exception {
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };

    Restaurant restaurant = new Restaurant(1L, "Restaurant",
        new Address("Street 1", "Street 2", "City", "State", "Zip"), new RestaurantMenu(menuItems));

    GetRestaurantResponse response = new GetRestaurantResponse(restaurant.getId(), restaurant.getName());
    String urlTemplate = "/restaurants/" + restaurant.getId();

    when(service.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

    this.mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotFindRestaurantById() throws Exception {
    this.mockMvc.perform(get("/restaurants/1")).andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  public void shouldCreateRestaurant() throws Exception {
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };
    List<MenuItemDTO> menuItemsDTO = new ArrayList<>() {
      {
        add(new MenuItemDTO("1", "Cheeseburger", new Money("50.20")));
      }
    };

    Restaurant restaurant = new Restaurant(1L, "Restaurant",
        new Address("Street 1", "Street 2", "City", "State", "Zip"), new RestaurantMenu(menuItems));
    CreateRestaurantResponse response = new CreateRestaurantResponse(restaurant.getId());
    CreateRestaurantRequest request = new CreateRestaurantRequest(restaurant.getName(), restaurant.getAddress(),
        new RestaurantMenuDTO(menuItemsDTO));

    when(service.create(request)).thenReturn(restaurant);

    this.mockMvc
        .perform(post("/restaurants").content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotCreateInvalidRestaurant() throws Exception {
    mockMvc.perform(
        post("/restaurants").content(objectMapper.writeValueAsString(new CreateRestaurantRequest(null, null, null)))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/restaurants")
        .content(objectMapper
            .writeValueAsString(new CreateRestaurantRequest("Name", new Address(), new RestaurantMenuDTO())))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/restaurants")
        .content(objectMapper.writeValueAsString(
            new CreateRestaurantRequest("Name", new Address(), new RestaurantMenuDTO(new ArrayList<MenuItemDTO>()))))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/restaurants").content(objectMapper.writeValueAsString(
        new CreateRestaurantRequest("Name", new Address(), new RestaurantMenuDTO(new ArrayList<MenuItemDTO>() {
          {
            add(new MenuItemDTO(null, null, null));
          }
        })))).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

}
