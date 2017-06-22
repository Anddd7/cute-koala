package com.koala.daos;

import com.koala.services.HttpService;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public interface UserDao {

  String getName();

  HttpService getHttpService();
}
