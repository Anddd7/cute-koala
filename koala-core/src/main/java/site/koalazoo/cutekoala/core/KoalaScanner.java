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

  public static final List<String> outputPaths = new ArrayList<>();

  public static final String outputPath = ".*\\\\target\\\\(test-){0,1}classes\\\\";

  String getValidClasspath(String filepath) {
    //return filepath.replaceFirst(outputPath, "");
    for (String path : outputPaths) {
      if (filepath.startsWith(path)) {
        return filepath.substring(path.length()+1);
      }
    }
    return filepath;
  }


  public List<Class> scanClasspath(Class target) {
    try {
      URL url = target.getResource(target.getSimpleName() + ".class");
      if ("jar".equals(url.getProtocol())) {
        return scanJar(url);
      } else {
        return scanOutput(target);
      }
    } catch (IOException e) {
      throw KoalaException.invalidClasspath(e);
    }
  }

  public List<Class> scanJar(URL url) throws IOException {
    List<Class> classes = new ArrayList<>();

    log.info("已打包成jar");
    JarURLConnection con = (JarURLConnection) url.openConnection();
    JarFile archive = con.getJarFile();
    Enumeration<JarEntry> entries = archive.entries();
    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String classname = entry.getName();
      log.debug("查询到Jar中的文件 - {}", classname);
      if (classname.endsWith(".class")) {
        scanClass(classname).ifPresent(classes::add);
      }
    }

    return classes;
  }

  public List<Class> scanOutput(Class target) throws IOException {
    List<Class> classes = new ArrayList<>();

    Enumeration<URL> rootURLs = target.getClassLoader().getResources("");
    while (rootURLs.hasMoreElements()) {
      URL rootURL = rootURLs.nextElement();
      if ("file".equals(rootURL.getProtocol())) {
        File dir = new File(rootURL.getFile());
        log.debug("查询到根路径 - {}", dir.getPath());
        outputPaths.add(dir.getPath());
        scanDir(dir, classes);
      }
    }

    return classes;
  }


  public void scanDir(File dir, List<Class> classes) {
    for (File child : dir.listFiles()) {
      if (child.isDirectory()) {
        scanDir(child, classes);
      } else if (child.getName().endsWith(".class")) {
        String classpath = getValidClasspath(child.getPath());
        scanClass(classpath).ifPresent(classes::add);
      }
    }
  }

  public Optional<Class> scanClass(String path) {
    log.info("扫描到class - {}", path);
    String classname = path.substring(0, path.length() - 6).replaceAll("\\\\", ".");
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
      return Optional.of(clazz);
    }

    return Optional.empty();
  }
}
