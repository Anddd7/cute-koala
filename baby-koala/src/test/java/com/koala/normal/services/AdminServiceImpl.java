package com.koala.normal.services;

import com.koala.daos.UserDao;
import com.koala.daos.impl.AdminDaoImpl;
import github.koala.core.annotation.Koala;

/**
 * @author edliao on 2017/6/26.
 * @description TODO
 */
public class AdminServiceImpl {

  @Koala(AdminDaoImpl.class)
  UserDao userDao;

  public void welcome() {
    System.out.println("Welcome," + userDao.getName());
  }
}
