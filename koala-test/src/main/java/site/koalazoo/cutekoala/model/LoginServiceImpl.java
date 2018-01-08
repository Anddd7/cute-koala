package site.koalazoo.cutekoala.model;

import site.koalazoo.cutekoala.annotation.Koala;

/**
 * @author and777
 * @date 2018/1/5
 */
@Koala
public class LoginServiceImpl implements LoginService {

  @Override
  public void login(String name, String passwd) {
    System.out.println("login - " + name + ":" + passwd);
  }
}
