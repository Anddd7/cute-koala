package com.koala.rpc.services.impl;

import com.koala.rpc.beans.Order;
import com.koala.rpc.beans.User;
import com.koala.rpc.services.OrderService;
import com.koala.rpc.services.UserService;
import github.koala.annotation.KoalaImport;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

  @KoalaImport
  UserService userService;

  @Override
  public Order createOrder(User user) {
    if (userService.checkAge(user)) {
      return new Order(UUID.randomUUID().toString(), user);
    }
    return null;
  }
}
