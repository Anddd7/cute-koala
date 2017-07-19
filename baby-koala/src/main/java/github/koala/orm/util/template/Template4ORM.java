package github.koala.orm.util.template;

import com.google.common.base.CaseFormat;
import github.eddy.common.FileSystemTool;
import github.eddy.common.TemplateGenerator;
import github.koala.orm.util.meta.ColumnMeta;
import github.koala.orm.util.meta.TableMeta;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
@Data
public class Template4ORM {

  String sourcePath;
  String packageName;
  String pojoName;

  Set<String> importClasses = new HashSet<>();

  List<String> fieldClassNames = new ArrayList<>();
  List<String> fieldNames = new ArrayList<>();
  List<String> fieldLabels = new ArrayList<>();
  List<String> fieldNamesWithUpperCamel = new ArrayList<>();

  List<String> keyClassNames = new ArrayList<>();//for interface
  List<String> keyNames = new ArrayList<>();//for interface
  List<String> keyLabels = new ArrayList<>();
  List<String> keyNamesWithUpperCamel = new ArrayList<>();

  List<String> notKeyClassNames = new ArrayList<>();//for interface
  List<String> notKeyNames = new ArrayList<>();//for interface
  List<String> notKeyLabels = new ArrayList<>();
  List<String> notKeyNamesWithUpperCamel = new ArrayList<>();

  String schemaName;
  String tableName;

  public static Template4ORM from(String sourcePath, String packagePath, TableMeta tableMeta) {
    Template4ORM temp4POJO = new Template4ORM();

    temp4POJO.setSourcePath(sourcePath);
    temp4POJO.setPackageName(packagePath);
    temp4POJO.setPojoName(CaseFormat.LOWER_UNDERSCORE
        .to(CaseFormat.UPPER_CAMEL, tableMeta.getTableName()));

    tableMeta.getColumns().forEach(columnMeta -> temp4POJO.writeToList(columnMeta, 0));
    tableMeta.getPrimaryKeys().forEach(columnMeta -> temp4POJO.writeToList(columnMeta, 1));
    tableMeta.getNotKeyColumns().forEach(columnMeta -> temp4POJO.writeToList(columnMeta, 2));

    temp4POJO.setSchemaName(tableMeta.getSchemaName());
    temp4POJO.setTableName(tableMeta.getTableName());

    return temp4POJO;
  }

  public void writeToList(ColumnMeta columnMeta, int type) {
    String columnName = CaseFormat.LOWER_UNDERSCORE
        .to(CaseFormat.LOWER_CAMEL, columnMeta.getColumnName());

    String columnNameWithUpperCamel = CaseFormat.LOWER_UNDERSCORE
        .to(CaseFormat.UPPER_CAMEL, columnMeta.getColumnName());

    Class fieldClass = columnMeta.getColumnClass();

    if (fieldClass.getName().startsWith("java.sql.")) {
      this.getImportClasses().add(fieldClass.getName());
    }

    if (type == 0) {
      this.getFieldClassNames().add(fieldClass.getSimpleName());
      this.getFieldNames().add(columnName);
      this.getFieldLabels().add(columnMeta.getColumnName());
      this.getFieldNamesWithUpperCamel().add(columnNameWithUpperCamel);
    } else if (type == 1) {
      this.getKeyClassNames().add(fieldClass.getSimpleName());
      this.getKeyNames().add(columnName);
      this.getKeyLabels().add(columnMeta.getColumnName());
      this.getKeyNamesWithUpperCamel().add(columnNameWithUpperCamel);
    } else if (type == 2) {
      this.getNotKeyClassNames().add(fieldClass.getSimpleName());
      this.getNotKeyNames().add(columnName);
      this.getNotKeyLabels().add(columnMeta.getColumnName());
      this.getNotKeyNamesWithUpperCamel().add(columnNameWithUpperCamel);
    }
  }

  public void generateTo() {
    TemplateGenerator generator = new TemplateGenerator();

    String filePath =
        FileSystemTool.getProjectPath() + "/" + sourcePath + "/" + packageName.replace(".", "/");

    generator.generateTemplateTo(this, "template/orm-pojo.beelt", filePath + "/domian",
        pojoName + ".java");

    generator.generateTemplateTo(this, "template/orm-interface.beelt", filePath + "/dao",
        pojoName + "Dao.java");

    generator.generateTemplateTo(this, "template/orm-implement.beelt", filePath + "/dao/impl",
        pojoName + "DaoImpl.java");
  }
}
