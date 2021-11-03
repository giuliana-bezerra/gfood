package com.example.gfood.restaurantservice.domain;

import java.util.Optional;
import java.util.stream.Collectors;

import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;
import com.example.gfood.domain.RestaurantRepository;
import com.example.gfood.restaurantservice.api.CreateRestaurantRequest;
import com.example.gfood.restaurantservice.api.RestaurantMenuDTO;

import org.springframework.transaction.annotation.Transactional;

public class RestaurantService {
  private RestaurantRepository restaurantRepository;

  public RestaurantService(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

  public Optional<Restaurant> findById(Long restaurantId) {
    return restaurantRepository.findById(restaurantId);
  }

  @Transactional
  public Restaurant create(CreateRestaurantRequest request) {
    Restaurant restaurant = new Restaurant(request.getName(), request.getAddress(),
        makeRestaurantMenu(request.getMenu()));
    restaurantRepository.save(restaurant);
    return restaurant;
  }

  private RestaurantMenu makeRestaurantMenu(RestaurantMenuDTO menu) {
    return new RestaurantMenu(menu.getMenuItems().stream()
        .map(menuItemDTO -> new MenuItem(menuItemDTO.getId(), menuItemDTO.getName(), menuItemDTO.getPrice()))
        .collect(Collectors.toList()));
  }
}
