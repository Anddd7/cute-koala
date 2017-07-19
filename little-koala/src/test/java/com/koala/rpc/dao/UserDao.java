package com.koala.rpc.dao;

import com.alibaba.fastjson.JSON;
import com.koala.orm.dao.ActorDao;
import com.koala.orm.dao.impl.ActorDaoImpl;
import com.koala.rpc.beans.User;
import github.koala.annotation.KoalaLocal;

public class UserDao {

  @KoalaLocal(ActorDaoImpl.class)
  ActorDao actorDao;

  public void saveUser(User user) {
    System.out.println("saved user:" + JSON.toJSONString(actorDao.selectAll().get(0)));
  }
}
