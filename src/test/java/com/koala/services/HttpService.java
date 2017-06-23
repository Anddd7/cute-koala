package com.koala.services;


import com.koala.beans.User;
import github.koala.rpc.annotation.HttpKoala;
import github.koala.rpc.annotation.HttpKoalaMethod;
import github.koala.rpc.annotation.HttpKoalaMethod.HttpMethod;
import github.koala.rpc.annotation.HttpKoalaParameter;
import java.util.List;
import java.util.Map;


/**
 * @author edliao on 2017/6/21.
 * @description rpc
 */
@HttpKoala("http://localhost:9999/api")
public interface HttpService {

  @HttpKoalaMethod( "/user")
  User getUser(@HttpKoalaParameter("name") String name);

  @HttpKoalaMethod( "/users")
  User[] getUsers(@HttpKoalaParameter("names") String... names);

  @HttpKoalaMethod(value = "/userList", httpMethod = HttpMethod.POST)
  List<User> getUserList(@HttpKoalaParameter("names") List<String> names);

  @HttpKoalaMethod("/userMap")
  Map<String, User> getUserMap(@HttpKoalaParameter("name") String name);
}
