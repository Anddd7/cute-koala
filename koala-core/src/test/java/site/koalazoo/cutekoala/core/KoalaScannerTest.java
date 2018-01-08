package site.koalazoo.cutekoala.core;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.koalazoo.cutekoala.model.HelloServiceImpl;
import site.koalazoo.cutekoala.model.SayServiceImpl;

/**
 * @author and777
 * @date 2018/1/5
 */
public class KoalaScannerTest {

  KoalaScanner scanner;

  @Before
  public void before() {
    scanner = new KoalaScanner();
  }

  /**
   * 通过classloader直接获取file会获取到全路径 ,使用正则替换掉classpath部分
   */
  @Test
  public void scanClasspath() {
    scanner.scanClasspath(KoalaScannerTest.class);
  }

  @Test
  public void scanClass() {
    Assert
        .assertEquals(scanner.scanClass("site/koalazoo/cutekoala/model/HelloServiceImpl").get(),
            HelloServiceImpl.class);
    Assert.assertEquals(scanner.scanClass("site/koalazoo/other/OtherTests").isPresent(), false);
  }

  @Test
  public void scanDir() {
    String dir = System.getProperty("user.dir").replaceAll("\\\\", "/");
    String prefix = dir + "/target/test-classes";
    String filePath = prefix + "/site/koalazoo/cutekoala/model";

    List<Class> classes = new ArrayList<>();
    scanner.scanDir(prefix.length(), new File(filePath), classes);
    assertTrue(classes.contains(SayServiceImpl.class));
  }

  /**
   * 见koala-test#ToolsTest.scanJar
   */
//  @Test
//  public void scanJar() throws IOException {
//  }
}