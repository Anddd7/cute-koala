package github.koala.orm.util.template;

import github.eddy.common.FileSystemTool;
import github.eddy.common.TemplateGenerator;
import github.koala.orm.util.meta.TableMeta;
import java.util.ArrayList;
import java.util.List;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
public class Temp4Interface {
  String sourcePath;
  String packageName;
  String pojoName;
  List<String> methods = new ArrayList<>();
  List<String> importClasses = new ArrayList<>();

  public static Temp4POJO from(String sourcePath, String packagePath, TableMeta tableMeta) {

    return null;
  }

  public void generateTo(TemplateGenerator generator) {
    generator.generateTemplateTo(this,
        "template/orm-interface.beelt",
        FileSystemTool.getProjectPath() + "/" + sourcePath + "/" + packageName.replace(".", "/")
        , pojoName + ".java");
  }
}
