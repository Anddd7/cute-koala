package com.koala.services;

import com.koala.daos.UserDao;
import github.koala.core.annotation.Service;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */

public class UserService {

  @Service
  UserDao userDao;

  public void welcome() {
    System.out.println("Welcome," + userDao.getName());
  }
}
