package site.koalazoo.cutekoala;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import site.koalazoo.model.Hello;
import site.koalazoo.model.HelloImpl;
import site.koalazoo.model.IHello;

/**
 * @author and777
 * @date 2018/1/4
 */
public class OtherTests {


  /**
   * class的各种Name
   */
  @Test
  public void getClassName() {
    List<Class> classes = Arrays
        .asList(Hello.class, Hello.World.class, IHello.class, HelloImpl.class, Hello[].class);

    for (Class clazz : classes) {
      System.out.println(clazz.getSimpleName());//classname
      System.out.println(clazz.getName());//path+类名
      System.out.println(clazz.getCanonicalName());//path+父类$子类
      System.out.println(clazz.getTypeName());//数组类型会检测实际类型+[]
    }
  }

  /**
   * 通过CanonicalName查询类
   * @throws ClassNotFoundException
   */
  @Test
  public void classForName() throws ClassNotFoundException {
    assertEquals(Hello.World.class, Class.forName("site.koalazoo.model.Hello$World"));
    try {
      Class.forName("site.koalazoo.model.Hello.World");
    } catch (ClassNotFoundException e) {
      assertTrue(e != null);
    }
  }

}
