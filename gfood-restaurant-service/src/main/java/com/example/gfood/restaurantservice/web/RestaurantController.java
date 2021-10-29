package com.example.gfood.restaurantservice.web;

import com.example.gfood.restaurantservice.api.CreateRestaurantRequest;
import com.example.gfood.restaurantservice.api.CreateRestaurantResponse;
import com.example.gfood.restaurantservice.api.GetRestaurantResponse;
import com.example.gfood.restaurantservice.domain.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/restaurants")
public class RestaurantController {
  @Autowired
  private RestaurantService restaurantService;

  @RequestMapping(method = RequestMethod.GET, path = "/{restaurantId}", produces = {
      "application/json; charset=UTF-8" })
  public ResponseEntity<GetRestaurantResponse> get(@PathVariable Long restaurantId) {
    return restaurantService.findById(restaurantId)
        .map(restaurant -> new ResponseEntity<>(new GetRestaurantResponse(restaurant.getId(), restaurant.getName()),
            HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }

  @RequestMapping(method = RequestMethod.POST)
  public CreateRestaurantResponse create(@RequestBody CreateRestaurantRequest request) {
    return new CreateRestaurantResponse(restaurantService.create(request).getId());
  }
}
