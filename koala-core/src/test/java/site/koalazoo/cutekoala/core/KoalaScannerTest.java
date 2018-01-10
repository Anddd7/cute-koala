package site.koalazoo.cutekoala.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.koalazoo.cutekoala.PathTool;
import site.koalazoo.cutekoala.builder.FilePath;
import site.koalazoo.cutekoala.model.HelloServiceImpl;
import site.koalazoo.cutekoala.model.SayServiceImpl;

/**
 * @author and777
 * @date 2018/1/5
 */
public class KoalaScannerTest {

  KoalaScanner scanner;
  private static final String MODULE_DIR = PathTool.getProjectPath();

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
    Assert.assertEquals(
        scanner.scanClass("site/koalazoo/cutekoala/model/HelloServiceImpl").get(),
        HelloServiceImpl.class);
    Assert.assertEquals(scanner.scanClass("site/koalazoo/other/OtherTests").isPresent(), false);
  }

  @Test
  public void scanDir() {
    FilePath prefix = FilePath.of(MODULE_DIR).child("target", "test-classes");
    FilePath filePath = prefix.child("site", "koalazoo", "cutekoala", "model");

    List<Class> classes = new ArrayList<>();
    scanner.scanDir(prefix.length(), filePath.getFile(), classes);
    assertTrue(classes.contains(SayServiceImpl.class));
  }

  /**
   * 需要用打包后的Jar测试 ,把打包后的jar放到outJars
   */
  @Test
  public void scanJar() throws IOException {
    KoalaScanner scanner = new KoalaScanner();
    URL jarUrl = FilePath.of(MODULE_DIR).parent("outJars").getURL4Jar("koala-tool-0.1.0.jar");
    List<Class> classes = scanner.scanJar(jarUrl);
    assertEquals(0, classes.size());
  }
}