package com.koala;

import com.koala.services.AdminServiceImpl;
import com.koala.webservice.HttpService;
import com.koala.services.UserService;
import com.koala.services.impl.UserServiceImpl;
import com.koala.utils.RandomTool;
import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Koala.ScopeEnum;
import github.koala.core.annotation.Module;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Module
public class UserModule {

  @Koala
  HttpService httpService;

  @Koala(UserServiceImpl.class)
  UserService userService;

  @Koala(scope = ScopeEnum.NOSCOPE)
  RandomTool randomTool;

  @Koala
  AdminServiceImpl adminService;
}
