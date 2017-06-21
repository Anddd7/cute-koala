package github.koala.core;

import com.koala.UserModule;
import com.koala.daos.UserDao;
import com.koala.services.UserService;
import com.koala.utils.RandomTool;
import github.koala.core.factory.BeanFactory;
import github.koala.core.scan.BeanScanner;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public class BeanFactoryTest {

  @Test
  public void scanTest() {
    BeanScanner scanner = new BeanScanner();
    scanner.scanModule(UserModule.class);
  }
  
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

  @Test
  public void multiTest() {
    BeanFactory beanFactory = BeanFactory.of(UserModule.class);
    UserDao userDao = beanFactory.getBean(UserDao.class);
    UserDao userDao1 = beanFactory.getBean(RandomTool.class).getUserDao();
    UserDao userDao2 = beanFactory.getBean(UserService.class).getUserDao();

    System.out.println(userDao.getName()+"|"+userDao1.getName()+"|"+userDao2.getName());

    System.out.println(userDao+"|"+userDao1+"|"+userDao2);

    Assert.assertEquals(userDao,userDao1);
    Assert.assertEquals(userDao1,userDao2);
    Assert.assertEquals(userDao,userDao2);
  }

}
