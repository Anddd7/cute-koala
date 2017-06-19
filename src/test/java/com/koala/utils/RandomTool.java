package com.koala.utils;

import java.util.Date;
import java.util.Random;
import lombok.Data;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Data
public class RandomTool {

  Integer intValue;
  Random r;

  public RandomTool() {
    r = new Random(new Date().getTime());
  }
  
  public RandomTool nextInteger() {
    intValue = r.nextInt();
    return this;
  }
}
