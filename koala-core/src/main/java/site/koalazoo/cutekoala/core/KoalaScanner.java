package site.koalazoo.cutekoala.core;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.extern.slf4j.Slf4j;
import site.koalazoo.cutekoala.annotation.Koala;
import site.koalazoo.cutekoala.common.KoalaException;

/**
 * @author and777
 * @date 2018/1/3
 *
 * 扫描classpath
 */
@Slf4j
public class KoalaScanner {

  private static final String FILE_PROTOCOL = "file";
  private static final String JAR_PROTOCOL = "jar";

  private static final String CLASS_SUFFIX = ".class";

  /**
   * 通过入口class判断目标代码是否打包成jar
   *
   * @param target 入口class
   */
  public List<Class> scanClasspath(Class target) {
    try {
      URL url = target.getResource(target.getSimpleName() + CLASS_SUFFIX);
      if (JAR_PROTOCOL.equals(url.getProtocol())) {
        return scanJar(url);
      } else {
        return scanOutput(target);
      }
    } catch (IOException e) {
      throw KoalaException.invalidClasspath(e);
    }
  }

  /**
   * 扫描jar中的class文件
   *
   * @param url jar路径
   */
  public List<Class> scanJar(URL url) throws IOException {
    List<Class> classes = new ArrayList<>();

    log.info("已打包成jar - {}", url.toString());
    JarURLConnection con = (JarURLConnection) url.openConnection();
    JarFile archive = con.getJarFile();
    Enumeration<JarEntry> entries = archive.entries();
    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String classpath = entry.getName();
      if (classpath.endsWith(CLASS_SUFFIX)) {
        classpath = classpath.substring(0, classpath.length() - 6);
        log.debug("查询到Jar中的class文件 - {}", classpath);
        scanClass(classpath).ifPresent(classes::add);
      }
    }

    return classes;
  }

  /**
   * 扫描classpath下的文件，因为测试时classpath可能有多个根目录，后面的方法需要替换根路径class。forname
   */
  public List<Class> scanOutput(Class target) throws IOException {
    List<Class> classes = new ArrayList<>();

    Enumeration<URL> rootURLs = target.getClassLoader().getResources("");
    while (rootURLs.hasMoreElements()) {
      URL rootURL = rootURLs.nextElement();
      if (FILE_PROTOCOL.equals(rootURL.getProtocol())) {
        File dir = new File(rootURL.getFile());
        String rootPath = dir.getPath();
        log.debug("查询到根路径 - {}", rootPath);
        scanDir(rootPath.length(), dir, classes);
      }
    }

    return classes;
  }

  /**
   * 扫描classpath下的所有class文件
   *
   * @param prefixLength classpath根路径长度，用来截取字串
   * @param dir          当前文件
   * @param classes      经过筛选的的classes
   */
  public void scanDir(int prefixLength, File dir, List<Class> classes) {
    for (File child : dir.listFiles()) {
      if (child.isDirectory()) {
        scanDir(prefixLength, child, classes);
      } else if (child.getName().endsWith(CLASS_SUFFIX)) {
        String filePath = child.getPath();
        String classPath = filePath.substring(prefixLength + 1, filePath.length() - 6);
        log.debug("查询到classpath中的class - {}", classPath);
        scanClass(classPath).ifPresent(classes::add);
      }
    }
  }

  /**
   * 通过class路径获取class
   *
   * @param path class相对路径
   */
  public Optional<Class> scanClass(String path) {
    String classname;

    if (path.indexOf('\\') > 0) {
      classname = path.replaceAll("\\\\", ".");
    } else {
      classname = path.replaceAll("/", ".");
    }

    Class clazz;
    try {
      clazz = Class.forName(classname);
    } catch (ClassNotFoundException e) {
      try {
        clazz = ClassLoader.getSystemClassLoader().loadClass(classname);
      } catch (ClassNotFoundException e1) {
        throw KoalaException.notExist(classname, e1);
      }
    }

    Koala koalaMark = (Koala) clazz.getDeclaredAnnotation(Koala.class);
    if (koalaMark != null) {
      log.debug("检测到koala - {}", classname);
      return Optional.of(clazz);
    }

    return Optional.empty();
  }
}
