package site.koalazoo.cutekoala.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.koalazoo.cutekoala.bean.KoalaManager;
import site.koalazoo.cutekoala.model.HelloService;
import site.koalazoo.cutekoala.model.HelloServiceImpl;
import site.koalazoo.cutekoala.model.SayService;
import site.koalazoo.cutekoala.model.SayServiceImpl;

/**
 * @author and777
 * @date 2018/1/9
 */
public class KoalaPoolTest {

  KoalaManager pool;

  @Before
  public void before() {
    pool = new KoalaManager();
  }

  @Test
  public void addKoala() {
    pool.addKoala(HelloServiceImpl.class);
    pool.addKoala(SayServiceImpl.class);

    HelloService hello1 = pool.getKoala(HelloServiceImpl.class);
    HelloService hello2 = pool.getKoala(HelloService.class);
    SayService say1 = pool.getKoala(SayServiceImpl.class);
    SayService say2 = pool.getKoala(SayService.class);

    Assert.assertEquals(hello1,hello2);
    Assert.assertEquals(say1,say2);
    Assert.assertEquals(hello1.getSayService(),say2);


    hello1.sayHello();
    hello2.sayHello();
    say1.say("you");
    say2.say("you");
  }
}
