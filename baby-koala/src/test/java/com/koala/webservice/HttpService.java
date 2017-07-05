package com.koala.webservice;


import com.koala.beans.User;
import github.koala.webservice.resetful.annotation.HttpKoala;
import github.koala.webservice.resetful.annotation.HttpKoalaMethod;
import github.koala.webservice.resetful.annotation.HttpKoalaParameter;
import github.koala.webservice.resetful.domain.HttpMethodEnum;
import java.util.List;
import java.util.Map;


/**
 * @author edliao on 2017/6/21.
 * @description rpc
 */
@HttpKoala("http://localhost:9999/api")
public interface HttpService {

  @HttpKoalaMethod("/user")
  User getUser(@HttpKoalaParameter("name") String name);

  @HttpKoalaMethod("/users")
  User[] getUsers(@HttpKoalaParameter("names") String... names);

  @HttpKoalaMethod(value = "/userList", httpMethod = HttpMethodEnum.POST)
  List<User> getUserList(@HttpKoalaParameter("names") List<String> names);

  @HttpKoalaMethod("/userMap")
  Map<String, User> getUserMap(@HttpKoalaParameter("name") String name);
}
