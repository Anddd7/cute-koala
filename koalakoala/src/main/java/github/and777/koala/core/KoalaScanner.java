package github.and777.koala.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * @author and777
 * @date 2018/1/3
 */
public class KoalaScanner {

  private final ClassLoader classLoader = KoalaScanner.class.getClassLoader();

  public void scanClasspath(String classpath) throws IOException {
    Enumeration<URL> rootURLs = classLoader.getResources("");
    while (rootURLs.hasMoreElements()) {
      URL rootURL = rootURLs.nextElement();
      if ("file".equals(rootURL.getProtocol())) {
        scanDir(rootURL.getPath());
      }
    }
  }

  public void scanDir(String path){
    File file = new File(path);

  }

}
