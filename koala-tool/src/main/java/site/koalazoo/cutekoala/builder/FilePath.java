package site.koalazoo.cutekoala.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import site.koalazoo.cutekoala.PathTool;

/**
 * @author and777
 * @date 2018/1/9
 *
 * 提供路径的便捷操作 ,方法实现参考{@link PathTool}
 */
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class FilePath {

  private final String path;

  public static FilePath of(String path) {
    return new FilePath(PathTool.replaceWindowsSeperator(path));
  }

  public int length() {
    return path.length();
  }

  /**
   * 切换路径 ,返回新路径的对象
   */
  public FilePath parent() {
    return new FilePath(PathTool.getParent(path()));
  }

  public FilePath parent(String... childs) {
    return parent().child(childs);
  }

  public FilePath child(String... childs) {
    return new FilePath(PathTool.getChild(path(), childs));
  }

  public URL getURL4Jar(String jarName) throws MalformedURLException {
    return PathTool.getURL4Jar(path, jarName);
  }

  public File getFile() {
    return new File(path);
  }

}