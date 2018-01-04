package site.koalazoo.cutekoala.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author and777
 * @date 2018/1/3
 */
@Slf4j
public class KoalaScanner {

  private final ClassLoader classLoader = KoalaScanner.class.getClassLoader();

  public void scanClasspath(String classpath) throws IOException {
    Enumeration<URL> rootURLs = classLoader.getResources("");
    while (rootURLs.hasMoreElements()) {
      URL rootURL = rootURLs.nextElement();
      if ("file".equals(rootURL.getProtocol())) {
        File dir = new File(rootURL.getFile());
        scanDir(dir);
      }
    }
  }


  public void scanDir(File dir) {
    for (File child : dir.listFiles()) {
      if (child.isDirectory()) {
        scanDir(child);
      } else if (child.getName().endsWith(".class")) {
        scanClass(child.getPath());
      }
    }
  }

  public void scanClass(String path) {
    log.info("扫描到class - {}", path);
    /**
     * TODO 截取classpath ,使用class.forname加载.
     */
  }

}
