package site.koalazoo.cutekoala;

import lombok.experimental.UtilityClass;

/**
 * @author and777
 * @date 2018/1/10
 */
@UtilityClass
public class StringTool {

  /**
   * 截取字串的前缀后缀
   */
  public String cutstring(String s, int prefixLength, int suffixLength) {
    return s.substring(prefixLength, s.length() - suffixLength);
  }
}
