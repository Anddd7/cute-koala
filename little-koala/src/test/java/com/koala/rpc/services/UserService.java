package com.koala.rpc.services;

import com.koala.rpc.beans.User;

public interface UserService {

  String getName(User user);

  User getUser(String name, int age);

  Boolean checkAge(User user);
}
