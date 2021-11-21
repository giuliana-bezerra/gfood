package com.example.gfood.orderservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.common.PersonName;
import com.example.gfood.consumerservice.domain.ConsumerNotFoundException;
import com.example.gfood.consumerservice.domain.ConsumerOrderInvalidException;
import com.example.gfood.consumerservice.domain.ConsumerService;
import com.example.gfood.domain.Consumer;
import com.example.gfood.domain.ConsumerRepository;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.OrderRepository;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;
import com.example.gfood.domain.RestaurantRepository;
import com.example.gfood.orderservice.api.OrderItemDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { OrderService.class, ConsumerService.class })
@AutoConfigureMockMvc
public class OrderServiceTest {
  @Autowired
  private OrderService service;

  @MockBean
  private OrderRepository orderRepository;

  @MockBean
  private RestaurantRepository restaurantRepository;

  @MockBean
  private ConsumerRepository consumerRepository;

  @Test
  public void shouldCreateOrder() {
    List<OrderItemDTO> orderItems = new ArrayList<>() {
      {
        add(new OrderItemDTO("1", 1));
      }
    };
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };
    Restaurant restaurant = new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems));
    Consumer consumer = new Consumer(1L, new PersonName("firstName", "lastName"));

    when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
    when(consumerRepository.findById(consumer.getId())).thenReturn(Optional.of(consumer));

    Order order = service.create(consumer.getId(), restaurant.getId(), orderItems);

    assertEquals(new Order(consumer.getId(), new Restaurant(restaurant.getId()), new ArrayList<>() {
      {
        add(new OrderItem("1", "Cheeseburger", new Money("50.20"), 1));
      }
    }), order);
  }

  @Test
  public void shouldNotCreateOrderWithInvalidRestaurant() {
    assertThrows(RestaurantNotFoundException.class, () -> service.create(1L, 1L, new ArrayList<>()));
  }

  @Test
  public void shouldNotCreateOrderWithInvalidConsumer() {
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };

    Restaurant restaurant = new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems));
    when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

    assertThrows(ConsumerNotFoundException.class, () -> service.create(1L, restaurant.getId(), new ArrayList<>()));
  }

  @Test
  public void shouldNotCreateOrderWithInvalidItem() {
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };

    List<OrderItemDTO> orderItems = new ArrayList<>() {
      {
        add(new OrderItemDTO("2", 1));
      }
    };

    Consumer consumer = new Consumer(1L, new PersonName("firstName", "lastName"));
    Restaurant restaurant = new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems));

    when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
    when(consumerRepository.findById(consumer.getId())).thenReturn(Optional.of(consumer));

    assertThrows(InvalidMenuItemException.class, () -> service.create(1L, restaurant.getId(), orderItems));
  }

  @Test
  public void shouldNotCreateInvalidConsumerOrder() {
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };
    List<OrderItemDTO> orderItems = new ArrayList<>() {
      {
        add(new OrderItemDTO("1", -1));
      }
    };

    Consumer consumer = new Consumer(1L, new PersonName("firstName", "lastName"));
    Restaurant restaurant = new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems));

    when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
    when(consumerRepository.findById(consumer.getId())).thenReturn(Optional.of(consumer));

    assertThrows(ConsumerOrderInvalidException.class,
        () -> service.create(consumer.getId(), restaurant.getId(), orderItems));
  }

  @Test
  public void shouldCancelOrder() {
    Order order = new Order(1L, 1L, new Restaurant(1L), new ArrayList<>());

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    assertEquals(service.cancel(1L), Optional.of(order));
  }

  @Test
  public void shouldNotCancelOrder() {
    when(orderRepository.findById(1L)).thenReturn(Optional.empty());

    assertEquals(service.cancel(1L), Optional.empty());
  }
}
