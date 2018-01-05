package site.koalazoo.cutekoala;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import site.koalazoo.cutekoala.core.KoalaFactory;

/**
 * @author and777
 * @date 2018/1/5
 *
 * 测试打包Jar后扫描ClassPath的功能
 */
public class Main {

  public static void main(String[] args) throws IOException {
    KoalaFactory.run(Main.class);
  }
}
