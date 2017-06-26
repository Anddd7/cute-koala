package others.fileio;

import github.koala.common.FileSystemTool;
import github.koala.common.PatternTool;
import org.junit.Test;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */
public class PathTest {

  @Test
  public void getPath() {
    System.out.println(FileSystemTool.getAbsoluteClassPath());
  }

  @Test
  public void convertString() {
    System.out.println(PatternTool.format2UpperCamel("userService"));
    System.out.println(PatternTool.format2UpperCamel("user-service"));
    System.out.println(PatternTool.format2UpperCamel("user_service"));
    System.out.println(PatternTool.format2UpperCamel("UserService"));
    System.out.println(PatternTool.format2UpperCamel("USER-SERVICE"));
    System.out.println(PatternTool.format2UpperCamel("USER_SERVICE"));
  }
}
