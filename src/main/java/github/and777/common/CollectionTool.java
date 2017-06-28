package github.and777.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author edliao on 2017/6/23.
 * @description 数组List工具补充
 */
public class CollectionTool {

  private CollectionTool() {
  }

  /**
   * 返回List 数组第一个符合条件的元素 ,没有则返回Optional.empty()
   */
  public static <T> Optional<T> getFirst(T[] objs, Predicate<T> predicate) {
    return getFirst(Arrays.asList(objs), predicate);
  }

  public static <T> Optional<T> getFirst(List<T> objs, Predicate<T> predicate) {
    for (T obj : objs) {
      if (predicate.test(obj)) {
        return Optional.ofNullable(obj);
      }
    }
    return Optional.empty();
  }

  /**
   * 对数组第一个符合条件的元素执行回调
   */
  public static <T> void dealFirst(T[] objs, Predicate<T> predicate, Consumer<T> consumer) {
    getFirst(objs, predicate).ifPresent(consumer);
  }

  public static <T> void dealFirst(List<T> objs, Predicate<T> predicate, Consumer<T> consumer) {
    getFirst(objs, predicate).ifPresent(consumer);
  }

  /**
   * 数组判空
   */
  public static Boolean isEmpty(Object[] objs) {
    return objs == null || objs.length == 0;
  }

  /**
   * 获取Map中第一个符合要求的value值
   */
  public static <K, V> Optional<V> getFirst(Map<K, V> map, Predicate<K> predicate) {
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey())) {
        return Optional.ofNullable(entry.getValue());
      }
    }
    return Optional.empty();
  }

  /**
   * 处理Map中第一个符合要求的value值
   */
  public static <K, V> void dealFirst(Map<K, V> map, Predicate<K> predicate, Consumer<V> consumer) {
    getFirst(map, predicate).ifPresent(consumer);
  }
}
