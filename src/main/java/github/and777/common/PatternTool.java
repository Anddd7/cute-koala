package github.and777.common;

import com.google.common.base.CaseFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author edliao on 2017/6/26.
 * @description 模式匹配
 */
public class PatternTool {

  private PatternTool() {
  }

  final static Pattern Lower_Camel = Pattern.compile("^[a-z]+([A-Z][a-z]*)*$");//patternTool
  final static Pattern Lower_Hyphen = Pattern.compile("^[a-z]+(-[a-z]+)*$");//pattern-tool
  final static Pattern Lower_UnderScore = Pattern.compile("^[a-z]+(_[a-z]+)*$");//pattern_tool

  final static Pattern Upper_Camel = Pattern.compile("^[A-Z][a-z]*([A-Z][a-z]*)*$");//PatternTool
  final static Pattern Upper_Hyphen = Pattern.compile("^[A-Z]+(-[A-Z]+)*$");//PATTERN-TOOL
  final static Pattern Upper_UnderScore = Pattern.compile("^[A-Z]+(_[A-Z]+)*$");//PATTERN_TOOL

  final static Map<Pattern, CaseFormat> pattern2CaseFormatMap = new HashMap<>();

  static {
    pattern2CaseFormatMap.put(Lower_Camel, CaseFormat.LOWER_CAMEL);
    pattern2CaseFormatMap.put(Lower_Hyphen, CaseFormat.LOWER_HYPHEN);
    pattern2CaseFormatMap.put(Lower_UnderScore, CaseFormat.LOWER_UNDERSCORE);
    pattern2CaseFormatMap.put(Upper_Camel, CaseFormat.UPPER_CAMEL);
    pattern2CaseFormatMap.put(Upper_Hyphen, null);
    pattern2CaseFormatMap.put(Upper_UnderScore, CaseFormat.UPPER_UNDERSCORE);
  }

  public static String format2UpperCamel(String name) {
    Optional<CaseFormat> caseFormat = getCurrentCaseFormat(name);
    if (caseFormat.isPresent()) {
      return caseFormat.get().to(CaseFormat.UPPER_CAMEL, name);
    }
    return name;
  }

  public static Optional<CaseFormat> getCurrentCaseFormat(String name) {
    return CollectionTool
        .getFirst(pattern2CaseFormatMap, pattern -> pattern.matcher(name).matches());
  }
}
