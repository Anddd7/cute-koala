package com.koala.mvc;

import com.alibaba.fastjson.JSON;
import com.koala.rpc.beans.User;
import com.koala.rpc.services.UserService;
import github.koala.mvc.KoalaServlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KoalaServletTest extends KoalaServlet {

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    UserService userService = tree.getKoala(UserService.class);
    User user = userService.getUser("eddy", 24);
    resp.getWriter().write(JSON.toJSONString(user));
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doPost(req, resp);
  }
}

