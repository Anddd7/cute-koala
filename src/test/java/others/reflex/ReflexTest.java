package others.reflex;

import com.koala.beans.User;
import java.lang.reflect.Constructor;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;

/**
 * @author edliao on 2017/6/21.
 * @description 需要分开运行测试 ,第一次new过后 User.class 加载到JVM后 ,直接使用User.class.instance会快很多 Class forName
 * 需要在classPath寻找 class 所以很慢 ,
 *
 * 实际上是 new(1)<class.newInstance(1.75)<Class.forName(3.8) ,
 *
 * PS.但很多时候Class文件 和 反射方法 可以缓存 ,就可以减少查找的时间 ,实际构建的时间相差很小
 */
public class ReflexTest {

  @Before
  public void test() {
    Class u = User.class;
  }

  @Test
  public void testNew() {
    printTestInfo("普通 new", () -> {
      User user = new User();
      user.toString();
    });
  }

  @Test
  public void testReflex() {
    printTestInfo("反射 new", () -> {
      try {
        User user = User.class.newInstance();
        user.toString();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @Test
  public void testClassForName() {
    printTestInfo("反射 forName new", () -> {
      try {
        User user = (User) Class.forName(User.class.getName()).newInstance();
        user.toString();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  @Test
  public void testReflexCache() {
    printTestInfoByMethod("缓存 反射  new", () -> {
      try {
        return User.class.getConstructor();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }, (constructor) -> {
      try {
        User user = constructor.newInstance();
        user.toString();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }


  @Test
  public void testClassForNameCache() throws ClassNotFoundException {
    printTestInfoByClass("缓存 反射 forName new", () -> {
      try {
        return Class.forName(User.class.getName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }, (classType) -> {
      try {
        User user = (User) classType.newInstance();
        user.toString();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  void printTestInfo(String tips, Runnable runnable) {
    long start = System.currentTimeMillis();
    for (int i = 0; i < 10000000; i++) {
      runnable.run();
    }
    System.out.println(tips + ":" + (System.currentTimeMillis() - start));
  }

  <T> void printTestInfoByClass(String tips, Supplier<Class<T>> supplier,
      Consumer<Class<T>> consumer) {
    long start = System.currentTimeMillis();
    Class<T> classType = supplier.get();
    for (int i = 0; i < 10000000; i++) {
      consumer.accept(classType);
    }
    System.out.println(tips + ":" + (System.currentTimeMillis() - start));
  }

  <T> void printTestInfoByMethod(String tips, Supplier<Constructor<T>> supplier,
      Consumer<Constructor<T>> consumer) {
    long start = System.currentTimeMillis();
    Constructor<T> constructor = supplier.get();
    for (int i = 0; i < 10000000; i++) {
      consumer.accept(constructor);
    }
    System.out.println(tips + ":" + (System.currentTimeMillis() - start));
  }
}
