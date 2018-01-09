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

  private static final String MODULE_DIR = PathTool.getProjectPath();

  /**
   * 测试是否能检测到Jar中的koala ,并forName成功
   */
  @Test
  public void scanJar() throws IOException {
    String dir = PathTool.getParent(MODULE_DIR) + "/outJars";
    KoalaScanner scanner = new KoalaScanner();
    URL jarUrl = PathTool.getURL4Jar(dir, "koala-test-0.1.0.jar");
    List<Class> classes = scanner.scanJar(jarUrl);
    assertEquals(2, classes.size());
  }
}
