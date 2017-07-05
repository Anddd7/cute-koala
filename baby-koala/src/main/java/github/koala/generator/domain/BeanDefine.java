package github.koala.generator.domain;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;

/**
 * @author edliao on 2017/6/23.
 * @description 自动生成代码时对Bean的描述pojo
 */
public class BeanDefine {

  @Setter
  @Getter
  String beanName;
  @Setter
  @Getter
  String path;

  @Setter
  @Getter
  String annotation;

  @Getter
  String implPath;

  @Getter
  String scope;

  private static final String NOSCOPE = "scope = github.koala.core.annotation.Koala.ScopeEnum.NOSCOPE";

  public void setScope(String scope) {
    this.scope = scope;
    if (Strings.isNullOrEmpty(annotation)) {
      annotation = "(" + NOSCOPE + ")";
    } else {
      annotation = annotation.substring(0, annotation.length() - 1)
          + "," + NOSCOPE + ")";
    }
  }

  public void setImplPath(String implPath) {
    this.implPath = implPath;
    if (Strings.isNullOrEmpty(annotation)) {
      annotation = "(value = " + implPath + ".class)";
    } else {
      annotation =
          annotation.substring(0, annotation.length() - 1) + ",value = " + implPath + ".class)";
    }
  }
}
