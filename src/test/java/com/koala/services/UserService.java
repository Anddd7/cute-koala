package com.koala.services;

import com.koala.daos.UserDao;
import com.koala.utils.RandomTool;
import github.koala.core.annotation.Koala;
import github.koala.core.annotation.Koala.ScopeEnum;
import github.koala.core.annotation.Module;
import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Module
@Data
public class UserService {

  @Koala
  UserDao userDao;

  @Koala(ScopeEnum.NOSCOPE)
  RandomTool randomTool;

  public void welcome() {
    System.out.println("Welcome," + userDao.getName());
  }
}
