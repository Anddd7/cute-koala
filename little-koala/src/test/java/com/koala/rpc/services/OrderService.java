package com.koala.rpc.services;


import com.koala.rpc.beans.Order;
import com.koala.rpc.beans.User;

public interface OrderService {

  Order createOrder(User user);
}
