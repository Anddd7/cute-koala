package com.koala.normal;

import com.koala.normal.services.Service1;
import com.koala.normal.services.Service2;
import com.koala.normal.services.impl.Service1Impl;
import github.koala.annotation.KoalaExport;
import github.koala.annotation.KoalaLocal;

public class Module {

  @KoalaLocal(Service1Impl.class)
  Service1 service1;

  @KoalaLocal
  @KoalaExport
  Service2 service2;
}
