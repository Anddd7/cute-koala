package site.koalazoo.cutekoala.model;

import site.koalazoo.cutekoala.annotation.Koala;
import site.koalazoo.cutekoala.annotation.KoalaImport;

/**
 * @author and777
 * @date 2018/1/5
 */
@Koala
public class HelloServiceImpl implements HelloService {

  @KoalaImport
  SayService sayService;

  @Override
  public void sayHello() {
    sayService.say("Hello");
  }
}
