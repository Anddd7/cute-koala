package github.koala;

import com.koala.UserModule;
import com.koala.daos.UserDao;
import com.koala.daos.impl.UserDaoImpl;
import com.koala.normal.services.AdminServiceImpl;
import com.koala.normal.services.UserService;
import com.koala.normal.services.impl.UserServiceImpl;
import com.koala.utils.RandomTool;
import com.koala.webservice.HttpService;
import github.koala.core.bean.BeanWrapper;
import github.koala.core.factory.KoalaFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
@Slf4j
public class BeanFactoryTest {

  @Test
  public void factoryTest() {
    KoalaFactory beanFactory = KoalaFactory.of(UserModule.class);
    HttpService httpService = beanFactory.getBean(HttpService.class);
    UserService userService = beanFactory.getBean(UserService.class);
    RandomTool randomTool = beanFactory.getBean(RandomTool.class);

    httpService.getUser("eddy");
    userService.welcome();
    randomTool.getUserDao().getName();

    UserService userService1 = beanFactory.getBean(UserServiceImpl.class);

    Assert.assertEquals(userService, userService1);

    UserDao userDao = beanFactory.getBean(UserDao.class);
    UserDao userDao2 = beanFactory.getBean(UserDaoImpl.class);

    Assert.assertEquals(userDao, userDao2);
    Assert.assertEquals(userService.getUserDao(), userService1.getUserDao());
    Assert.assertEquals(userDao2, userService1.getUserDao());
    Assert.assertNotEquals(randomTool, userService1.getRandomTool());

    System.out.println("通过Factory.getBean");
    printObj(userService);
    printObj(userService1);

    printObj(userDao);
    printObj(userService.getUserDao());
    printObj(userDao2);

    printObj(httpService);

    printObj(randomTool);
    printObj(userService1.getRandomTool());

    System.out.println("容器:");
    beanFactory.getCache4Test().forEach(this::printBeanWrapper);
  }

  void printObj(Object object) {
    print(object.getClass(), object);
  }

  void printBeanWrapper(Class classType, BeanWrapper beanWrapper) {
    print(classType, beanWrapper.getInstance());
  }

  void print(Class classType, Object object) {
    System.out.println(classType.getName() + "\t|\t" + object);
  }

  @Test
  public void differentImpl() {
    KoalaFactory factory = KoalaFactory.of(UserModule.class);
    factory.getBean(AdminServiceImpl.class).welcome();
  }
}
