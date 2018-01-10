package site.koalazoo.cutekoala;

import java.net.MalformedURLException;
import java.net.URL;
import lombok.experimental.UtilityClass;

/**
 * @author and777
 * @date 2018/1/9
 *
 * 因为windows环境(开发)的路径有盘符标识 ,并且使用反斜杠分隔路径 ,不利于实际运行环境(linux)
 */
@UtilityClass
public class PathTool {

  public final String OS_NAME = System.getProperty("os.name");
  public final String MODULE_DIR = System.getProperty("user.dir");

  public final String SEPERATOR = "/";
  public final String CLASS_SEPERATOR = ".";

  public String getProjectPath() {
    return replaceWindowsSeperator(MODULE_DIR);
  }

  /**
   * 替换windows中的文件路径符
   *
   * @param path 目标路径
   */
  public String replaceWindowsSeperator(String path) {
    if (path.contains("\\")) {
      path = path.replaceAll("\\\\", SEPERATOR);
    }
    return path;
  }

  /**
   * 替换windows中的文件路径符
   *
   * @param path 目标路径
   */
  public String replaceClassSeperator(String path) {
    return replaceWindowsSeperator(path).replaceAll(SEPERATOR, CLASS_SEPERATOR);
  }

  /**
   * 获取父目录 ,如果是根节点则返回本身
   */
  public String getParent(String path) {
    int indexOfLastSeperator = path.lastIndexOf(SEPERATOR);
    boolean isWindowsRoot = path.indexOf(':') == indexOfLastSeperator - 1;
    boolean isUnixRoot = indexOfLastSeperator == 0;
    if (isWindowsRoot || isUnixRoot) {
      return path;
    }
    return path.substring(0, path.lastIndexOf(SEPERATOR));
  }

  public String getChild(String path, String... childs) {
    return path + SEPERATOR + String.join(SEPERATOR, childs);
  }

  /**
   * 获取jar的URL地址 ,window路径是"/D:/xxx" ,而linux是"/xxx"
   *
   * @param dir     文件夹路径
   * @param jarName jar名称
   */
  public URL getURL4Jar(String dir, String jarName) throws MalformedURLException {
    if (dir.contains(":")) {
      dir = SEPERATOR + dir;
    }
    return new URL("jar:file:" + dir + SEPERATOR + jarName + "!" + SEPERATOR);
  }

  /**
   * 去掉文件后缀
   */
  private String removeSuffix(String path) {
    int index = path.lastIndexOf('.');
    if (path.lastIndexOf(SEPERATOR) < index) {
      return path.substring(0, index);
    }
    return path;
  }
}
