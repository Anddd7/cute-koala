package com.koala;

import com.koala.services.UserService;
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
  UserService userService;

  @Koala(ScopeEnum.NOSCOPE)
  RandomTool randomTool;
}
