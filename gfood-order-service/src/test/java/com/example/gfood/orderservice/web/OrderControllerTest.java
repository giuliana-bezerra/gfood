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

import com.example.gfood.common.DateType;
import com.example.gfood.common.MoneyModuleConfig;
import com.example.gfood.common.OrderState;
import com.example.gfood.common.UnsupportedStateTransitionException;
import com.example.gfood.courierservice.domain.NoCouriersAvailableException;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderAcceptance;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.OrderRevision;
import com.example.gfood.orderservice.OrderConstants;
import com.example.gfood.orderservice.api.AcceptOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.api.OrderItemDTO;
import com.example.gfood.orderservice.api.ReviseOrderRequest;
import com.example.gfood.orderservice.domain.OrderService;
import com.example.gfood.orderservice.main.OrderServiceConfig;
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

  @Autowired
  private OrderController controller;

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
    Order order = new Order(OrderConstants.ORDER);

    GetOrderResponse response = controller.makeGetOrderResponse(order);
    String urlTemplate = "/orders/" + order.getId();

    when(service.findById(order.getId())).thenReturn(Optional.of(order));

    this.mockMvc.perform(get(urlTemplate)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotFindOrderById() throws Exception {
    this.mockMvc.perform(get("/orders/1")).andDo(print()).andExpect(status().isNoContent());
  }

  @Test
  public void shouldCreateOrder() throws Exception {
    Order order = new Order(OrderConstants.ORDER);

    List<OrderItemDTO> orderItemsDTO = new ArrayList<>() {
      {
        add(new OrderItemDTO(order.getOrderItems().get(0).getMenuItemId(), 1));
      }
    };

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
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.CANCELLED);

    when(service.cancel(order.getId())).thenReturn(Optional.of(order));

    GetOrderResponse response = controller.makeGetOrderResponse(order);

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
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.CANCELLED);

    when(service.cancel(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc.perform(post("/orders/1/cancel")).andDo(print()).andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void shouldReviseOrder() throws Exception {
    Integer revisedQuantity = 2;
    Order order = new Order(OrderConstants.ORDER);
    OrderItem orderItem = order.getOrderItems().get(0);
    List<OrderItem> orderItemsRevised = new ArrayList<>() {
      {
        add(new OrderItem(orderItem.getMenuItemId(), orderItem.getName(), orderItem.getPrice(), revisedQuantity));
      }
    };
    Order revisedOrder = new Order(order.getId(), order.getConsumerId(), order.getRestaurant(), orderItemsRevised);
    revisedOrder.setOrderState(OrderState.APPROVED);

    OrderRevision orderRevision = new OrderRevision(new HashMap<>() {
      {
        put(orderItem.getMenuItemId(), revisedQuantity);
      }
    });
    ReviseOrderRequest request = new ReviseOrderRequest(new HashMap<>() {
      {
        put(orderItem.getMenuItemId(), revisedQuantity);
      }
    });
    GetOrderResponse response = controller.makeGetOrderResponse(revisedOrder);

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
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.ACCEPTED);
    LocalDateTime readyBy = LOCAL_DATE_TIME.plusHours(1L);
    AcceptOrderRequest request = new AcceptOrderRequest(readyBy);
    GetOrderResponse response = controller.makeGetOrderResponse(order);
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
    when(service.accept(3L, new OrderAcceptance(readyBy))).thenThrow(NoCouriersAvailableException.class);
    when(service.accept(4L, new OrderAcceptance(readyBy))).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(post("/orders/1/accept").content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound());

    mockMvc.perform(post("/orders/1/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest()))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    mockMvc.perform(post("/orders/2/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest(readyBy)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());

    mockMvc.perform(post("/orders/3/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest(readyBy)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());

    mockMvc.perform(post("/orders/4/accept").content(objectMapper.writeValueAsString(new AcceptOrderRequest(readyBy)))
        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isUnprocessableEntity());
  }

  @Test
  public void shouldStartPreparingOrder() throws Exception {
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.PREPARING);

    GetOrderResponse response = controller.makeGetOrderResponse(order);

    when(service.preparing(order.getId())).thenReturn(Optional.of(order));

    mockMvc
        .perform(post("/orders/1/preparing"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotStartPreparingOrder() throws Exception {
    when(service.preparing(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc
        .perform(post("/orders/1/preparing"))
        .andDo(print()).andExpect(status().isUnprocessableEntity());
    mockMvc
        .perform(post("/orders/2/preparing"))
        .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void shouldStartReadyForPickupOrder() throws Exception {
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.READY_FOR_PICKUP);

    GetOrderResponse response = controller.makeGetOrderResponse(order);

    when(service.readyForPickup(order.getId())).thenReturn(Optional.of(order));

    mockMvc
        .perform(post("/orders/1/ready"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotStartReadyForPickupOrder() throws Exception {
    when(service.readyForPickup(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc
        .perform(post("/orders/1/ready"))
        .andDo(print()).andExpect(status().isUnprocessableEntity());
    mockMvc
        .perform(post("/orders/2/ready"))
        .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void shouldStartPickedUpOrder() throws Exception {
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.PICKED_UP);

    GetOrderResponse response = controller.makeGetOrderResponse(order);

    when(service.pickedUp(order.getId())).thenReturn(Optional.of(order));

    mockMvc
        .perform(post("/orders/1/pickedup"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotStartPickedUpOrder() throws Exception {
    when(service.pickedUp(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc
        .perform(post("/orders/1/pickedup"))
        .andDo(print()).andExpect(status().isUnprocessableEntity());
    mockMvc
        .perform(post("/orders/2/pickedup"))
        .andDo(print()).andExpect(status().isNotFound());
  }

  @Test
  public void shouldStartDeliveredOrder() throws Exception {
    Order order = new Order(OrderConstants.ORDER);
    order.setOrderState(OrderState.DELIVERED);

    GetOrderResponse response = controller.makeGetOrderResponse(order);

    when(service.delivered(order.getId())).thenReturn(Optional.of(order));

    mockMvc
        .perform(post("/orders/1/delivered"))
        .andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  public void shouldNotStartDeliveredOrder() throws Exception {
    when(service.delivered(1L)).thenThrow(UnsupportedStateTransitionException.class);

    mockMvc
        .perform(post("/orders/1/delivered"))
        .andDo(print()).andExpect(status().isUnprocessableEntity());
    mockMvc
        .perform(post("/orders/2/delivered"))
        .andDo(print()).andExpect(status().isNotFound());
  }
}
