package com.koala.services;

import com.koala.services.beans.User;
import github.koala.core.annotation.HttpKoala;
import github.koala.core.annotation.HttpKoalaMethod;
import github.koala.core.annotation.HttpKoalaMethod.Request;
import github.koala.core.annotation.HttpKoalaParameter;
import java.util.List;
import java.util.Map;

/**
 * @author edliao on 2017/6/21.
 * @description rpc
 */
@HttpKoala("http://localhost:9999/api")
public interface HttpService {

  @HttpKoalaMethod(value = "/user")
  User getUser(@HttpKoalaParameter("name") String name);

  @HttpKoalaMethod(value = "/users")
  User[] getUsers(@HttpKoalaParameter("names") String... names);

  @HttpKoalaMethod(value = "/userList", type = Request.POST)
  List<User> getUserList(@HttpKoalaParameter("names") List<String> names);

  @HttpKoalaMethod("/userMap")
  Map<String, User> getUserMap(@HttpKoalaParameter("name") String name);
}
