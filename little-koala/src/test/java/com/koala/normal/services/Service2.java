package com.koala.normal.services;

import com.koala.normal.services.impl.Service3Impl;
import github.koala.annotation.KoalaLocal;
import github.koala.annotation.KoalaLocal.ScopeEnum;

public class Service2 {

  @KoalaLocal(value = Service3Impl.class, scope = ScopeEnum.NOSCOPE)
  Service3 service3;

  public void say() {
    System.out.println("service2");
  }

  public void say3() {
    service3.say();
  }
}
