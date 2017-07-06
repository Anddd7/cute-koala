package com.koala.services.impl;

import com.koala.services.RpcSecond;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public class RpcSecondImpl implements RpcSecond {

  @Override
  public String welcome(String name) {
    return "Welcome " + name;
  }
}
