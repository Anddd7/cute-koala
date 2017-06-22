package com.koala.services.impl;

import com.koala.daos.UserDao;
import com.koala.daos.impl.UserDaoImpl;
import com.koala.services.UserService;
import com.koala.utils.RandomTool;
import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Koala.ScopeEnum;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public class UserServiceImpl implements UserService {

  @Koala( UserDaoImpl.class)
  UserDao userDao;

  @Koala(scope = ScopeEnum.NOSCOPE)
  RandomTool randomTool;

  public void welcome() {
    System.out.println("Welcome," + userDao.getName());
  }

  @Override
  public UserDao getUserDao() {
    return userDao;
  }

  @Override
  public RandomTool getRandomTool() {
    return randomTool;
  }

}
