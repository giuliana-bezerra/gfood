package com.example.gfood.orderservice.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.example.gfood.common.Address;
import com.example.gfood.common.DateType;
import com.example.gfood.common.Money;
import com.example.gfood.common.MoneyModuleConfig;
import com.example.gfood.common.UnsupportedStateTransitionException;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderAcceptance;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.OrderRevision;
import com.example.gfood.domain.OrderState;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;
import com.example.gfood.orderservice.api.AcceptOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.api.OrderItemDTO;
import com.example.gfood.orderservice.api.ReviseOrderRequest;
import com.example.gfood.orderservice.domain.OrderService;
import com.example.gfood.orderservice.main.OrderServiceConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(classes = { OrderServiceConfig.class })
@WebMvcTest(OrderController.class)
@Import(value = { OrderController.class, MoneyModuleConfig.class })
public class OrderControllerTest {
  private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService service;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DateType dateType;

  @BeforeEach
  public void init() {
    when(dateType.now()).thenReturn(LOCAL_DATE_TIME);
  }

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
        order.getRestaurant().getName(), OrderState.APPROVED.name());
    String urlTemplate = "/orders/" + order.getId();

    when(service.findById(1L)).thenReturn(Optional.of(order));

    this.mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotFindOrderById() throws Exception {
    this.mockMvc.perform(get("/orders/1")).andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  public void shouldCreateOrder() throws Exception {
    List<OrderItem> orderItems = new ArrayList<>() {
      {
        add(new OrderItem("1", "Cheeseburger", new Money("50.20"), 1));
      }
    };
    List<OrderItemDTO> orderItemsDTO = new ArrayList<>() {
      {
        add(new OrderItemDTO("1", 1));
      }
    };
    List<MenuItem> menuItems = new ArrayList<>() {
      {
        add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
      }
    };

    Order order = new Order(1L, 1L, new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems)),
        orderItems);

    CreateOrderRequest request = new CreateOrderRequest(order.getRestaurant().getId(), order.getConsumerId(),
        orderItemsDTO);
    CreateOrderResponse response = new CreateOrderResponse(order.getId());

    when(service.create(request.getConsumerId(), request.getRestaurantId(), request.getOrderItems())).thenReturn(order);

    mockMvc
        .perform(
            post("/orders").content(objectMapper.writeValueAsString(request)).contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isCreated())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotCreateInvalidOrder() throws Exception {
    mockMvc.perform(post("/orders").content(objectMapper.writeValueAsString(new CreateOrderRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders").content(objectMapper.writeValueAsString(new CreateOrderRequest(null, null, null)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(new CreateOrderRequest(0L, 0L, new ArrayList<OrderItemDTO>())))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(new CreateOrderRequest(1L, 1L, new ArrayList<OrderItemDTO>() {
          {
            add(new OrderItemDTO());
          }
        }))).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders")
        .content(objectMapper.writeValueAsString(new CreateOrderRequest(1L, 1L, new ArrayList<OrderItemDTO>() {
          {
            add(new OrderItemDTO(null, 0));
          }
        }))).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldCancelOrder() throws Exception {
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
    order.setOrderState(OrderState.CANCELLED);

    when(service.cancel(order.getId())).thenReturn(Optional.of(order));

    GetOrderResponse response = new GetOrderResponse(order.getId(), order.getOrderTotal(),
        order.getRestaurant().getName(), OrderState.CANCELLED.name());

    mockMvc.perform(post("/orders/1/cancel")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotCancelOrder() throws Exception {
    when(service.cancel(1L)).thenReturn(Optional.empty());

    mockMvc.perform(post("/orders/1/cancel")).andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void shouldNotCancelInvalidStateOrder() throws Exception {
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
    order.setOrderState(OrderState.CANCELLED);

    when(service.cancel(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc.perform(post("/orders/1/cancel")).andDo(print()).andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void shouldReviseOrder() throws Exception {
    Integer revisedQuantity = 2;
    List<OrderItem> orderItems = new ArrayList<>() {
      {
        add(new OrderItem("1", "Cheeseburger", new Money("50.20"), 1));
      }
    };
    List<OrderItem> orderItemsRevised = new ArrayList<>() {
      {
        add(new OrderItem("1", "Cheeseburger", new Money("50.20"), revisedQuantity));
      }
    };
    Order order = new Order(1L, 1L, new Restaurant(1L, "name", new Address()), orderItems);
    Order revisedOrder = new Order(1L, 1L, new Restaurant(1L, "name", new Address()), orderItemsRevised);

    OrderRevision orderRevision = new OrderRevision(new HashMap<>() {
      {
        put("1", revisedQuantity);
      }
    });
    ReviseOrderRequest request = new ReviseOrderRequest(new HashMap<>() {
      {
        put("1", revisedQuantity);
      }
    });
    GetOrderResponse response = new GetOrderResponse(revisedOrder.getId(), revisedOrder.getOrderTotal(),
        revisedOrder.getRestaurant().getName(), OrderState.APPROVED.name());

    when(service.revise(order.getId(), orderRevision)).thenReturn(Optional.of(revisedOrder));

    mockMvc
        .perform(post("/orders/1/revise").content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotReviseOrder() throws Exception {
    OrderRevision orderRevision = new OrderRevision(new HashMap<>() {
      {
        put("1", 1);
      }
    });
    ReviseOrderRequest request = new ReviseOrderRequest(new HashMap<>() {
      {
        put("1", 1);
      }
    });

    when(service.revise(1L, orderRevision)).thenReturn(Optional.empty());

    mockMvc.perform(post("/orders/1/revise").content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());

    mockMvc.perform(post("/orders/1/revise").content(objectMapper.writeValueAsString(new ReviseOrderRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void shouldAcceptOrder() throws Exception {
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
    order.setOrderState(OrderState.ACCEPTED);
    LocalDateTime readyBy = LOCAL_DATE_TIME.plusHours(1L);
    AcceptOrderRequest request = new AcceptOrderRequest(readyBy);
    GetOrderResponse response = new GetOrderResponse(order.getId(), order.getOrderTotal(),
        order.getRestaurant().getName(), OrderState.ACCEPTED.name());
    when(service.accept(order.getId(), new OrderAcceptance(readyBy))).thenReturn(Optional.of(order));

    mockMvc
        .perform(post("/orders/1/accept").content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotAcceptOrder() throws Exception {
    LocalDateTime readyBy = LOCAL_DATE_TIME.plusHours(1L);
    AcceptOrderRequest request = new AcceptOrderRequest(readyBy);

    when(service.accept(1L, new OrderAcceptance())).thenReturn(Optional.empty());
    when(service.accept(2L, new OrderAcceptance(readyBy))).thenThrow(UnsupportedStateTransitionException.class);
    when(service.accept(3L, new OrderAcceptance(readyBy))).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(post("/orders/1/accept").content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());

    mockMvc.perform(post("/orders/1/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders/2/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest(readyBy)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
    mockMvc.perform(post("/orders/3/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest(readyBy)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
  }
}
