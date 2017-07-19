package com.koala.normal.services.impl;

import com.koala.normal.services.RpcFirst;

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
