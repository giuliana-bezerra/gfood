package com.example.gfood;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.common.OrderState;
import com.example.gfood.common.PersonName;
import com.example.gfood.consumerservice.api.CreateConsumerRequest;
import com.example.gfood.consumerservice.api.CreateConsumerResponse;
import com.example.gfood.consumerservice.api.GetConsumerResponse;
import com.example.gfood.courierservice.api.CreateCourierRequest;
import com.example.gfood.courierservice.api.GetCourierResponse;
import com.example.gfood.courierservice.api.UpdateCourierRequest;
import com.example.gfood.domain.ActionType;
import com.example.gfood.orderservice.api.AcceptOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderRequest;
import com.example.gfood.orderservice.api.CreateOrderResponse;
import com.example.gfood.orderservice.api.GetOrderResponse;
import com.example.gfood.orderservice.api.OrderItemDTO;
import com.example.gfood.orderservice.api.ReviseOrderRequest;
import com.example.gfood.restaurantservice.api.CreateRestaurantRequest;
import com.example.gfood.restaurantservice.api.CreateRestaurantResponse;
import com.example.gfood.restaurantservice.api.GetRestaurantResponse;
import com.example.gfood.restaurantservice.api.MenuItemDTO;
import com.example.gfood.restaurantservice.api.RestaurantMenuDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@SpringBootTest(classes = { GfoodEndToEndTests.Config.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
public class GfoodEndToEndTests extends GfoodServices {
	private final static LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();

	public static final String MENU_ITEM_ID = "1";
	public static final String MENU_ITEM_NAME = "Chicken Vindaloo";
	public static final Money MENU_ITEM_PRICE = new Money("12.34");
	public static final Integer MENU_ITEM_QUANTITY = 1;
	public static final Integer MENU_ITEM_QUANTITY_REVISED = 2;
	public static final String RESTAURANT_NAME = "My Restaurant";
	private static final Address RESTAURANT_ADDRESS = new Address("1 High Street", null, "Oakland", "CA", "94619");

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Override
	public String getHost() {
		return "localhost";
	}

	@Override
	public int getApplicationPort() {
		return port;
	}

	@EnableAutoConfiguration
	@ComponentScan
	public static class Config {
	}

	@BeforeEach
	public void init() {
		restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	@Test
	public void shouldCreateReviseAndCancelOrder() {
		Long orderId = createOrderProcess();
		reviseOrderProcess(orderId);
		cancelOrderProcess(orderId);
	}

	@Test
	public void shouldDeliverOrder() {
		Long courierId = createCourierProcess();
		Long orderId = createOrderProcess();
		acceptOrderProcess(orderId, courierId);
		preparingOrderProcess(orderId);
		orderReadyForPickupProcess(orderId);
		pickupOrderProcess(orderId);
		deliverOrderProcess(orderId);
	}

	private Long createOrderProcess() {
		Long consumerId = createConsumer();
		verifyConsumerCreated(consumerId);

		Long restaurantId = createRestaurant();
		verifyRestaurantCreated(restaurantId);

		Long orderId = createOrder(consumerId, restaurantId);
		verifyOrderApproved(orderId);
		verifyOrderByConsumer(consumerId);

		return orderId;
	}

	private Long createConsumer() {
		CreateConsumerRequest request = new CreateConsumerRequest(new PersonName("firstName", "lastName"));
		CreateConsumerResponse response = this.restTemplate.postForObject(consumerBaseUrl(), request,
				CreateConsumerResponse.class);
		assertNotNull(response);
		Long consumerId = response.getId();
		assertTrue(consumerId > 0);
		return consumerId;
	}

	private void verifyConsumerCreated(Long consumerId) {
		GetConsumerResponse response = this.restTemplate.getForObject(consumerBaseUrl(consumerId.toString()),
				GetConsumerResponse.class);
		assertNotNull(response);
		assertEquals(response.getId(), consumerId);
	}

	private Long createRestaurant() {
		List<MenuItemDTO> menuItems = new ArrayList<>() {
			{
				add(new MenuItemDTO(MENU_ITEM_ID, MENU_ITEM_NAME, MENU_ITEM_PRICE));
			}
		};
		CreateRestaurantRequest request = new CreateRestaurantRequest(RESTAURANT_NAME, RESTAURANT_ADDRESS,
				new RestaurantMenuDTO(menuItems));
		CreateRestaurantResponse response = this.restTemplate.postForObject(restaurantBaseUrl(), request,
				CreateRestaurantResponse.class);
		Long restaurantId = response.getId();
		assertNotNull(response);
		assertTrue(restaurantId > 0);

		return restaurantId;
	}

	private void verifyRestaurantCreated(Long restaurantId) {
		GetRestaurantResponse response = this.restTemplate.getForObject(restaurantBaseUrl(restaurantId.toString()),
				GetRestaurantResponse.class);
		assertNotNull(response);
		assertEquals(restaurantId, response.getId());
	}

	private Long createOrder(Long consumerId, Long restaurantId) {
		List<OrderItemDTO> orderItems = new ArrayList<>() {
			{
				add(new OrderItemDTO(MENU_ITEM_ID, MENU_ITEM_QUANTITY));
			}
		};
		CreateOrderRequest request = new CreateOrderRequest(restaurantId, consumerId, orderItems);
		CreateOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(), request, CreateOrderResponse.class);

		assertNotNull(response);
		Long orderId = response.getId();
		assertTrue(orderId > 0);
		return orderId;
	}

	private void verifyOrderApproved(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.APPROVED.name(), response.getState());
	}

	private void verifyOrderByConsumer(Long consumerId) {
		ResponseEntity<List<GetOrderResponse>> response = this.restTemplate.exchange(
				orderBaseUrl() + "?consumerId=" + consumerId,
				HttpMethod.GET, null,
				new ParameterizedTypeReference<List<GetOrderResponse>>() {
				});
		assertNotNull(response.getBody());

		GetOrderResponse orderResponse = response.getBody().get(0);
		assertNotNull(orderResponse);
		assertEquals(RESTAURANT_NAME, orderResponse.getRestaurantName());
		assertEquals(MENU_ITEM_PRICE.multiply(MENU_ITEM_QUANTITY), orderResponse.getOrderTotal());
		assertEquals(OrderState.APPROVED.name(), orderResponse.getState());
	}

	private void reviseOrderProcess(Long orderId) {
		reviseOrder(orderId);
		verifyOrderRevised(orderId);
	}

	private void reviseOrder(Long orderId) {
		ReviseOrderRequest request = new ReviseOrderRequest(Collections.singletonMap(
				MENU_ITEM_ID, MENU_ITEM_QUANTITY_REVISED));
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "revise"), request,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(MENU_ITEM_PRICE.multiply(MENU_ITEM_QUANTITY_REVISED), response.getOrderTotal());
	}

	private void verifyOrderRevised(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(MENU_ITEM_PRICE.multiply(MENU_ITEM_QUANTITY_REVISED), response.getOrderTotal());
	}

	private void cancelOrderProcess(Long orderId) {
		cancelOrder(orderId);
		verifyOrderCancelled(orderId);
	}

	private void cancelOrder(Long orderId) {
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "cancel"), null,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(OrderState.CANCELLED.name(), response.getState());
	}

	private void verifyOrderCancelled(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.CANCELLED.name(), response.getState());
	}

	private Long createCourierProcess() {
		Long courierId = createCourier();
		setCourierAvailable(courierId);
		verifyCourierAvailable(courierId);
		return courierId;
	}

	private Long createCourier() {
		CreateCourierRequest request = new CreateCourierRequest(new PersonName("firstName", "lastName"), new Address());
		GetCourierResponse response = this.restTemplate.postForObject(courierBaseUrl(), request, GetCourierResponse.class);
		assertNotNull(response);
		Long courierId = response.getId();
		assertTrue(courierId > 0);
		return courierId;
	}

	private void setCourierAvailable(Long courierId) {
		UpdateCourierRequest request = new UpdateCourierRequest(true);
		GetCourierResponse response = this.restTemplate
				.patchForObject(courierBaseUrl(courierId.toString()), request,
						GetCourierResponse.class);
		assertNotNull(response);
		assertEquals(courierId, response.getId());
		assertTrue(response.getAvailable());
	}

	private void verifyCourierAvailable(Long courierId) {
		GetCourierResponse response = this.restTemplate.getForObject(courierBaseUrl(courierId.toString()),
				GetCourierResponse.class);
		assertNotNull(response);
		assertEquals(courierId, response.getId());
		assertTrue(response.getAvailable());
	}

	private void acceptOrderProcess(Long orderId, Long courierId) {
		acceptOrder(orderId);
		verifyOrderAssignedToCourier(orderId, courierId);
	}

	private void acceptOrder(Long orderId) {
		AcceptOrderRequest request = new AcceptOrderRequest(LOCAL_DATE_TIME.plusHours(1L));
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "accept"), request,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.ACCEPTED.name(), response.getState());
	}

	private void verifyOrderAssignedToCourier(Long orderId, Long courierId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(courierId, response.getAssignedCourier());
		assertEquals(ActionType.PICKUP, response.getCourierActions().get(0).getType());
		assertEquals(ActionType.DROPOFF, response.getCourierActions().get(1).getType());
	}

	private void preparingOrderProcess(Long orderId) {
		preparingOrder(orderId);
		verifyPreparingOrder(orderId);
	}

	private void preparingOrder(Long orderId) {
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "preparing"), null,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.PREPARING.name(), response.getState());
	}

	private void verifyPreparingOrder(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.PREPARING.name(), response.getState());
	}

	private void orderReadyForPickupProcess(Long orderId) {
		orderReadyForPickup(orderId);
		verifyOrderReadyForPickup(orderId);
	}

	private void orderReadyForPickup(Long orderId) {
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "ready"), null,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.READY_FOR_PICKUP.name(), response.getState());
	}

	private void verifyOrderReadyForPickup(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.READY_FOR_PICKUP.name(), response.getState());
	}

	private void pickupOrderProcess(Long orderId) {
		pickupOrder(orderId);
		verifyOrderPickedUp(orderId);
	}

	private void pickupOrder(Long orderId) {
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "pickedup"), null,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.PICKED_UP.name(), response.getState());
	}

	private void verifyOrderPickedUp(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.PICKED_UP.name(), response.getState());
	}

	private void deliverOrderProcess(Long orderId) {
		deliverOrder(orderId);
		verifyOrderDelivered(orderId);
	}

	private void deliverOrder(Long orderId) {
		GetOrderResponse response = this.restTemplate.postForObject(orderBaseUrl(orderId.toString(), "delivered"), null,
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.DELIVERED.name(), response.getState());
	}

	private void verifyOrderDelivered(Long orderId) {
		GetOrderResponse response = this.restTemplate.getForObject(orderBaseUrl(orderId.toString()),
				GetOrderResponse.class);
		assertNotNull(response);
		assertEquals(orderId, response.getId());
		assertEquals(OrderState.DELIVERED.name(), response.getState());
	}
}
