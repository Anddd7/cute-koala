package github.koala.common;

import com.google.common.base.CaseFormat;
import java.util.regex.Pattern;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */
public class FileSystemTool {

  public static String getAbsoluteClassPath() {
    return Thread.currentThread().getContextClassLoader().getResource("").getPath();
  }

  public static String getProjectPath() {
    return System.getProperty("user.dir");
  }

}
