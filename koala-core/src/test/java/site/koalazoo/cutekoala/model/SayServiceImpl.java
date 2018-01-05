package site.koalazoo.cutekoala.model;

import site.koalazoo.cutekoala.annotation.Koala;

/**
 * @author and777
 * @date 2018/1/5
 */
@Koala
public class SayServiceImpl implements SayService {

  @Override
  public void say(String word) {
    System.out.println(word);
  }
}
