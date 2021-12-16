package com.example.gfood.orderservice;

import java.util.ArrayList;
import java.util.List;

import com.example.gfood.common.Address;
import com.example.gfood.common.Money;
import com.example.gfood.domain.MenuItem;
import com.example.gfood.domain.Order;
import com.example.gfood.domain.OrderItem;
import com.example.gfood.domain.Restaurant;
import com.example.gfood.domain.RestaurantMenu;

public class OrderConstants {
  public static final List<OrderItem> orderItems = new ArrayList<>() {
    {
      add(new OrderItem("1", "Cheeseburger", new Money("50.20"), 1));
    }
  };

  public static final List<MenuItem> menuItems = new ArrayList<>() {
    {
      add(new MenuItem("1", "Cheeseburger", new Money("50.20")));
    }
  };

  public static final Order ORDER = new Order(1L, 1L,
      new Restaurant(1L, "Grestaurant", new Address(), new RestaurantMenu(menuItems)),
      orderItems);
}
