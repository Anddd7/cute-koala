package com.koala.services.impl;

import com.koala.services.RpcFirst;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public class RpcFirstImpl implements RpcFirst {

  @Override
  public String hello(String name) {
    return "Hello " + name;
  }
}
