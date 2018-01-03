package github.and777.koala.core;

import java.io.IOException;

/**
 * @author and777
 * @date 2018/1/3
 *
 * 扫描/生成/获取 Koala的工厂
 */
public class KoalaFactory {

  public static final KoalaFactory FACTORY = new KoalaFactory();

  public static KoalaFactory run(String classpath) throws IOException {
    FACTORY.scanner.scanClasspath(classpath);

    return FACTORY;
  }


  /**
   * koala扫描器
   */
  private KoalaScanner scanner = new KoalaScanner();

}
