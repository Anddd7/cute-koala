package com.koala.rpc.services.impl;

import com.koala.rpc.beans.User;
import com.koala.rpc.dao.UserDao;
import com.koala.rpc.services.UserService;
import github.koala.annotation.KoalaLocal;
import java.util.UUID;

public class UserServiceImpl implements UserService {

  @KoalaLocal
  UserDao userDao;

  @Override
  public String getName(User user) {
    return user.getName();
  }

  @Override
  public User getUser(String name, int age) {
    User result = new User(UUID.randomUUID().toString(), name, age);
    userDao.saveUser(result);
    return result;
  }

  @Override
  public Boolean checkAge(User user) {
    return user.getAge() > 18 && user.getAge() < 60;
  }
}
