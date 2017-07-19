package com.koala.rpc;

import com.koala.rpc.services.UserService;
import com.koala.rpc.services.impl.UserServiceImpl;
import github.koala.annotation.KoalaExport;
import github.koala.annotation.KoalaLocal;

public class UserModule {

  @KoalaLocal(UserServiceImpl.class)
  @KoalaExport
  UserService userService;
}
