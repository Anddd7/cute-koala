package github.koala.common;

import com.google.common.base.CaseFormat;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */
public class IOTool {

  public static String getAbsoluteClassPath() {
    return Thread.currentThread().getContextClassLoader().getResource("").getPath();
  }

  public static String getProjectPath() {
    return System.getProperty("user.dir");
  }

  /**
   * TODO 根据名称里包含的_ - 大小写 自动判断 ,并生成需要的结果
   */
  public static String format(String name) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
  }
}
