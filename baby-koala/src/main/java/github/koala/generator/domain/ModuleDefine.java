package github.koala.generator.domain;

import github.and777.common.PatternTool;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author edliao on 2017/6/23.
 * @description 模块定义pojo
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
    this.moduleName = PatternTool.format2UpperCamel(moduleName) + "Module";
  }
}
