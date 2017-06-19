package github.koala.core;

import com.koala.UserModule;
import com.koala.services.UserService;
import com.koala.utils.RandomTool;
import github.koala.core.factory.BeanFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public class BeanFactoryTest {

  @Test
  public void factoryTest() {
    BeanFactory beanFactory = new BeanFactory(UserModule.class).build();
    UserService userService1 = beanFactory.getBean(UserService.class);
    UserService userService2 = beanFactory.getBean(UserService.class);

    userService1.welcome();
    Assert.assertEquals(userService1, userService2);

    RandomTool randomTool1 = beanFactory.getBean(RandomTool.class);
    RandomTool randomTool2 = beanFactory.getBean(RandomTool.class);

    System.out.println(randomTool1.nextInteger().getIntValue());
    System.out.println(randomTool2.nextInteger().getIntValue());
    Assert.assertNotEquals(randomTool1, randomTool2);

  }

}
