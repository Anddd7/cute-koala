package github.koala.generator.domain;

import com.google.common.base.CaseFormat;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author edliao on 2017/6/23.
 * @description TODO
 */


public class ModuleDefine {

  @Getter
  String moduleName;
  @Setter
  @Getter
  String packageName;
  @Setter
  @Getter
  List<BeanDefine> beans;

  public void setModuleName(String moduleName) {
    this.moduleName = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,moduleName);
  }
}
