package site.koalazoo.cutekoala;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.junit.Test;
import site.koalazoo.cutekoala.core.KoalaScanner;

/**
 * @author and777
 * @date 2018/1/7
 */
public class JarTest {

  /**
   * 需要用打包后的Jar测试
   */
  @Test
  public void scanJar() throws IOException {
    String dir = System.getProperty("user.dir").replaceAll("\\\\", "/");
    if('/' == dir.charAt(0)){
      dir = dir.substring(1);
    }
    KoalaScanner scanner = new KoalaScanner();
    URL jarUrl = new URL(
        "jar:file:/" + dir + "/outJars/koala-test-0.1.0.jar!/");
    List<Class> classes = scanner.scanJar(jarUrl);
    assertEquals(2, classes.size());
  }
}
