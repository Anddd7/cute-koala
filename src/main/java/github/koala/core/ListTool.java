package github.koala.core;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */
public class ListTool {

  public static <T> T getFirst(T[] objs, Predicate<T> predicate) {
    return getFirst(Arrays.asList(objs), predicate);
  }

  public static <T> T getFirst(List<T> objs, Predicate<T> predicate) {
    for (T obj : objs) {
      if (predicate.test(obj)) {
        return obj;
      }
    }
    return null;
  }

  public static <T> void dealFirst(T[] objs, Predicate<T> predicate, Consumer<T> consumer) {
    consumer.accept(getFirst(objs, predicate));
  }

  public static <T> void dealFirst(List<T> objs, Predicate<T> predicate, Consumer<T> consumer) {
    consumer.accept(getFirst(objs, predicate));
  }

  public static Boolean isEmpty(Object[] objs) {
    return objs == null || objs.length == 0;
  }
}
