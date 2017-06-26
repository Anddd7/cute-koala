package com.koala.daos.impl;

import com.koala.daos.UserDao;
import com.koala.webservice.HttpService;
import github.koala.core.annotation.Koala;

/**
 * @author edliao on 2017/6/26.
 * @description TODO
 */
public class AdminDaoImpl implements UserDao {

  @Koala
  HttpService httpService;

  public String getName() {
    return httpService.getUser("ADMIN").getName();
  }

  @Override
  public HttpService getHttpService() {
    return httpService;
  }
}
