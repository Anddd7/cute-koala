package site.koalazoo.cutekoala;

import java.net.MalformedURLException;
import java.net.URL;
import lombok.experimental.UtilityClass;

/**
 * @author and777
 * @date 2018/1/9
 *
 * 用于处理Path路径的工具类 ,函数式调用参考{@link site.koalazoo.cutekoala.builder.FilePath}
 */
@UtilityClass
public class PathTool {

  public final String OS_NAME = System.getProperty("os.name");
  public final String MODULE_DIR = System.getProperty("user.dir");
  public final boolean IS_WINDOWS = OS_NAME.contains("Windows");

  /**
   * 替换windows中的文件路径符
   *
   * @param path 目标路径
   */
  public String convert2LinuxPath(String path) {
    if (IS_WINDOWS) {
      path = path.replaceAll("\\\\", "/");
    }
    return path;
  }

  /**
   * 用.分隔的class路径
   *
   * @param path 目标路径
   */
  public String convert2ClassPath(String path) {
    if (path.indexOf('\\') > 0) {
      return path.replaceAll("\\\\", ".");
    }
    return path.replaceAll("/", ".");
  }

  public String getProjectPath() {
    return convert2LinuxPath(MODULE_DIR);
  }

  /**
   * 获取父目录 ,如果是根节点则返回本身
   */
  public String getParent(String path) {
    int indexOfLastSeperator = path.lastIndexOf('/');
    boolean isWindowsRoot = IS_WINDOWS && path.indexOf(':') == indexOfLastSeperator - 1;
    boolean isUnixRoot = indexOfLastSeperator == 0;
    if (isWindowsRoot || isUnixRoot) {
      return path;
    }
    return path.substring(0, path.lastIndexOf('/'));
  }

  public String getChild(String path, String... childs) {
    return path + "/" + String.join("/", childs);
  }

  /**
   * 获取jar的URL地址 ,window路径是"/D:/xxx" ,而linux是"/xxx"
   *
   * @param dir     文件夹路径
   * @param jarName jar名称
   */
  public URL getURL4Jar(String dir, String jarName) throws MalformedURLException {
    if (IS_WINDOWS) {
      dir = "/" + dir;
    }
    return new URL("jar:file:" + dir + "/" + jarName + "!/");
  }

  /**
   * 去掉文件后缀
   *
   * @param path 文件路径
   */
  private String removeSuffix(String path) {
    int index = path.lastIndexOf('.');
    if (path.lastIndexOf('/') < index) {
      return path.substring(0, index);
    }
    return path;
  }

}
