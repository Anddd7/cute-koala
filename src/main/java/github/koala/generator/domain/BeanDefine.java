package github.koala.generator.domain;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */

public class BeanDefine {

  @Setter
  @Getter
  String beanName;
  @Setter
  @Getter
  String path;

  @Getter
  String implPath;

  @Getter
  String scope;

  @Setter
  @Getter
  String annotation;

  public void checkAnnotation() {
    annotation = "(";
    List<String> params = new ArrayList<>();
    if (!Strings.isNullOrEmpty(implPath)) {
      params.add("value = " + implPath);
    }
    if (!Strings.isNullOrEmpty(scope) && !scope.equals("singleton")) {
      params.add("scope = ScopeEnum.NOSCOPE");
    }
    annotation += String.join(",", params);
    annotation += ")";
  }

  public void setScope(String scope) {
    this.scope = scope;
    if (Strings.isNullOrEmpty(annotation)) {
      annotation = "(scope = ScopeEnum.NOSCOPE)";
    } else {
      annotation = annotation.substring(0, annotation.length() - 1) + ",scope = ScopeEnum.NOSCOPE)";
    }
  }

  public void setImplPath(String implPath) {
    this.implPath = implPath;
    if (Strings.isNullOrEmpty(annotation)) {
      annotation = "(impl = " + implPath + ".class)";
    } else {
      annotation =
          annotation.substring(0, annotation.length() - 1) + ",impl = " + implPath + ".class)";
    }
  }
}
