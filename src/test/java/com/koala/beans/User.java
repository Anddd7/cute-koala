package com.koala.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author edliao on 2017/6/22.
 * @description TODO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

  Integer id;
  String name;

  @Override
  public String toString() {
    return id + ":" + name;
  }
}