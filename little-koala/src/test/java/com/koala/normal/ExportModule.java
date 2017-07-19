package com.koala.normal;

import com.koala.normal.services.Service1;
import com.koala.normal.services.impl.Service1Impl;
import github.koala.annotation.KoalaExport;
import github.koala.annotation.KoalaLocal;

public class ExportModule {

  @KoalaLocal(Service1Impl.class)
  @KoalaExport
  Service1 service1;
}
