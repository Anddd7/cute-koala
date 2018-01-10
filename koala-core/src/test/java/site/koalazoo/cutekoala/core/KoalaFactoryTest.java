package site.koalazoo.cutekoala.core;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import site.koalazoo.cutekoala.model.HelloService;
import site.koalazoo.cutekoala.model.HelloServiceImpl;
import site.koalazoo.cutekoala.model.SayService;
import site.koalazoo.cutekoala.model.SayServiceImpl;

/**
 * @author and777
 * @date 2018/1/3
 */
public class KoalaFactoryTest {

  @Test
  public void registerAndUser() throws IOException {
    KoalaFactory.run(KoalaFactoryTest.class);

    HelloService hello1 = KoalaFactory.getKoala(HelloServiceImpl.class);
    HelloService hello2 = KoalaFactory.getKoala(HelloService.class);
    SayService say1 = KoalaFactory.getKoala(SayServiceImpl.class);
    SayService say2 = KoalaFactory.getKoala(SayService.class);

    Assert.assertEquals(hello1, hello2);
    Assert.assertEquals(say1, say2);
    Assert.assertEquals(hello1.getSayService(), say2);

    hello1.sayHello();
    hello2.sayHello();
    say1.say("you");
    say2.say("you");
  }
}
