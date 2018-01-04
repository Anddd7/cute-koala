package site.koalazoo.cutekoala.bean;

import lombok.AllArgsConstructor;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean包装器
 */
@AllArgsConstructor
public class Koala<T> {
  String name;
  Class<T> clazz;
  T object;
}
