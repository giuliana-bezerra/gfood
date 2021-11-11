package com.example.gfood.orderservice.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.common.MoneyModuleConfig;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.domain.OrderService;
import com.example.gfood.orderservice.main.OrderServiceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = { OrderServiceConfig.class })
@WebMvcTest(OrderController.class)
@Import(value = { OrderController.class, MoneyModuleConfig.class })
public class OrderControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService service;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldFindOrderById() throws Exception {
    List<OrderItem> orderItems = new ArrayList<>() {
      {
        add(new OrderItem("1", "Cheeseburger", new Money("50.20"), 1));
      }
    };
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };

    Order order = new Order(1L, 1L, new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems)),
        orderItems);
    GetOrderResponse response = new GetOrderResponse(order.getId(), order.getOrderTotal(),
        order.getRestaurant().getName());
    String urlTemplate = "/orders/" + order.getId();

    when(service.findById(1L)).thenReturn(Optional.of(order));

    this.mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotFindOrderById() throws Exception {
    this.mockMvc.perform(get("/orders/1")).andDo(print()).andExpect(status().isNoContent());
  }
}
