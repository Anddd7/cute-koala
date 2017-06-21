package com.koala.utils;

import com.koala.daos.UserDao;
import github.koala.core.annotation.Module;
import github.koala.core.annotation.Koala;
import java.util.Date;
import java.util.Random;
import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description 测试
 */
@Data
@Module
public class RandomTool {

  Integer intValue;
  Random r;

  @Koala
  UserDao userDao;

  public RandomTool() {
    r = new Random(new Date().getTime());
  }

  public RandomTool nextInteger() {
    intValue = r.nextInt();
    return this;
  }

  public void printUser() {
    System.out.println(userDao.getName());
  }
}
