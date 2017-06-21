package com.koala.services;

import com.koala.daos.UserDao;
import com.koala.utils.RandomTool;
import github.koala.core.annotation.Module;
import github.koala.core.annotation.Scope;
import github.koala.core.annotation.Scope.ScopeEnum;
import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Module
@Data
public class UserService {

  @Scope
  UserDao userDao;

  @Scope(type = ScopeEnum.NOSCOPE)
  RandomTool randomTool;

  public void welcome() {
    System.out.println("Welcome," + userDao.getName());
  }
}
