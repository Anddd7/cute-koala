package com.koala.utils;

import com.koala.daos.UserDao;
import com.koala.daos.impl.UserDaoImpl;
import github.koala.core.annotation.Koala;
import java.util.Date;
import java.util.Random;

/**
 * @author edliao on 2017/6/19.
 * @description 测试
 */
public class RandomTool {

  Integer intValue;
  Random r;

  @Koala(UserDaoImpl.class)
  UserDao userDao;

  public RandomTool() {
    r = new Random(new Date().getTime());
  }

  public RandomTool nextInteger() {
    intValue = r.nextInt();
    return this;
  }

  public void printUser() {
    System.out.println(userDao.getName());
  }

  public UserDao getUserDao() {
    return userDao;
  }
}
