package site.koalazoo.cutekoala.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * @author and777
 * @date 2018/1/5
 */
public class KoalaScannerTest {

  /**
   * 通过classloader直接获取file会获取到全路径 ,使用正则替换掉classpath部分
   */
  @Test
  public void classpathPattern() {
    String path = "D:\\IDEASpace\\koalaman-core\\koala-core\\target\\classes\\";
    String file = "D:\\IDEASpace\\koalaman-core\\koala-core\\target\\classes\\site\\koalazoo\\cutekoala\\bean\\KoalaWrapper$1.class";

    String outputPath = ".*\\" +
        File.separator + "target\\" +
        File.separator + "(test-){0,1}classes\\" +
        File.separator;
    KoalaScanner scanner = new KoalaScanner();

    assertTrue(Pattern.compile(outputPath).matcher(path).matches());
    assertTrue(Pattern.compile(KoalaScanner.outputPath).matcher(path).matches());

    assertFalse(Pattern.compile(outputPath).matcher(file).matches());
    assertFalse(Pattern.compile(KoalaScanner.outputPath).matcher(file).matches());

    assertEquals(file.replace(path, ""), "site\\koalazoo\\cutekoala\\bean\\KoalaWrapper$1.class");
    assertEquals(scanner.getValidClasspath(file), file.replace(path, ""));
  }
}
