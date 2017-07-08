package github.koala.orm.util.template;

import com.google.common.base.CaseFormat;
import github.eddy.common.FileSystemTool;
import github.eddy.common.TemplateGenerator;
import github.koala.orm.util.meta.TableMeta;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Temp4POJO {

  String sourcePath;
  String packageName;
  String pojoName;
  List<String> fields = new ArrayList<>();
  Set<String> importClasses = new HashSet<>();

  public static Temp4POJO from(String sourcePath, String packagePath, TableMeta tableMeta) {
    Temp4POJO temp4POJO = new Temp4POJO();

    temp4POJO.setSourcePath(sourcePath);
    temp4POJO.setPackageName(packagePath+".domian");
    temp4POJO.setPojoName(CaseFormat.LOWER_UNDERSCORE
        .to(CaseFormat.UPPER_CAMEL, tableMeta.getTableName()));

    tableMeta.getColumns().forEach(columnMeta -> {
      String columnName = CaseFormat.LOWER_UNDERSCORE
          .to(CaseFormat.LOWER_CAMEL, columnMeta.getColumnName());
      Class fieldClass = columnMeta.getColumnClass();
      if (fieldClass.getName().startsWith("java.sql.")) {
        temp4POJO.getImportClasses().add(fieldClass.getName());
      }
      temp4POJO.getFields().add(fieldClass.getSimpleName() + " " + columnName);
    });

    return temp4POJO;
  }

  public void generateTo(TemplateGenerator generator) {
    generator.generateTemplateTo(this,
        "template/orm-pojo.beelt",
        FileSystemTool.getProjectPath() + "/" + sourcePath + "/" + packageName.replace(".", "/")
        , pojoName + ".java");
  }
}
