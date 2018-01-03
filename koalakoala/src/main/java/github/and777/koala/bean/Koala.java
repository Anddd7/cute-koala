package github.and777.koala.bean;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean包装器
 */
public class Koala<T> {
  String name;
  Class<T> clazz;
  T object;
}
