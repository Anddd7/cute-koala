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
  LoginService loginService;

  @Override
  public void sayHello(String name) {
    loginService.login(name, String.valueOf(name.hashCode()));
    System.out.println("Welcome," + name);
  }
}
