package com.koala;

import com.koala.services.UserService;
import com.koala.utils.RandomTool;
import github.koala.core.annotation.Module;
import github.koala.core.annotation.Scope;
import github.koala.core.annotation.Scope.ScopeEnum;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Module
public class UserModule {

  @Scope
  UserService userService;

  @Scope(type = ScopeEnum.NOSCOPE)
  RandomTool randomTool;
}
