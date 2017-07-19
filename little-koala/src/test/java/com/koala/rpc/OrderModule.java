package com.koala.rpc;

import com.koala.rpc.services.OrderService;
import com.koala.rpc.services.impl.OrderServiceImpl;
import github.koala.annotation.KoalaLocal;

public class OrderModule {

  @KoalaLocal(OrderServiceImpl.class)
  OrderService orderService;

}
