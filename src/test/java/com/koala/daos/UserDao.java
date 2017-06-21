package com.koala.daos;

import com.koala.services.HttpService;
import github.koala.core.annotation.Koala;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public class UserDao {

  @Koala
  HttpService httpService;

  public String getName() {
    return httpService.getUser("eddy").getName();
  }
}
