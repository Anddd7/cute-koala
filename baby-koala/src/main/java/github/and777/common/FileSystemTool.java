package github.and777.common;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;

/**
 * @author edliao on 2017/6/23.
 * @description 文件系统工具
 */
public class FileSystemTool {

  private FileSystemTool() {
  }

  /**
   * 获取classpath
   */
  public static String getAbsoluteClassPath() {
    return Thread.currentThread().getContextClassLoader().getResource("").getPath();
  }

  /**
   * 获取项目路径
   */
  public static String getProjectPath() {
    return System.getProperty("user.dir");
  }

  public static void writeFile(String filePath, String content) {
    try {
      Files.write(content.getBytes(), getFile(filePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static File getFile(String filePath) {
    File file = new File(filePath);
    try {
      if (!file.exists()) {
        Files.createParentDirs(file);
        file.createNewFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }

}
