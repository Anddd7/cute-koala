package com.koala.normal.services;

import com.koala.daos.UserDao;
import com.koala.utils.RandomTool;

/**
 * @author edliao on 2017/6/22.
 * @description TODO
 */
public interface UserService {

  void welcome();

  UserDao getUserDao();

  RandomTool getRandomTool();
}
