package com.koala.daos.impl;

import com.koala.daos.UserDao;
import com.koala.webservice.HttpService;
import github.koala.core.annotation.Koala;

/**
 * @author edliao on 2017/6/22.
 * @description TODO
 */
public class UserDaoImpl implements UserDao {

  @Koala
  HttpService httpService;

  public String getName() {
    return httpService.getUser("eddy").getName();
  }

  @Override
  public HttpService getHttpService() {
    return httpService;
  }
}