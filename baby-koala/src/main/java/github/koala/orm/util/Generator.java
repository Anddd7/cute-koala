package github.koala.orm.util;

import com.mysql.cj.core.result.Field;
import github.eddy.common.TemplateGenerator;
import github.koala.orm.conn.DBConnection;
import github.koala.orm.util.meta.ColumnMeta;
import github.koala.orm.util.meta.TableMeta;
import github.koala.orm.util.template.Template4ORM;
import java.util.Arrays;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
public class Generator {

  DBConnection connection;

  public Generator(DBConnection connection) {
    this.connection = connection;
  }

  public void generate(String sourcePath, String packagePath, String tableName) {
    TableMeta tableMeta = generateMeta(tableName);

    TemplateGenerator generator = new TemplateGenerator();

    Template4ORM temp4POJO = Template4ORM.from(sourcePath, packagePath, tableMeta);
    temp4POJO.generateTo(generator);
  }

  TableMeta generateMeta(String tableName) {
    TableMeta tableMeta = new TableMeta();
    tableMeta.setTableName(tableName);
    tableMeta.setSchemaName(connection.getSchema());

    Field[] fields = connection.fetchField(tableName);
    Arrays.asList(fields).forEach(field -> {
      ColumnMeta columnMeta = new ColumnMeta();
      columnMeta.setColumnName(field.getName());
      columnMeta.setColumnType(field.getMysqlType().getName());
      try {
        columnMeta.setColumnClass(Class.forName(field.getMysqlType().getClassName()));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }

      if (field.isPrimaryKey()) {
        tableMeta.addPrimaryKey(columnMeta);
      } else {
        tableMeta.addNotKeyColumns(columnMeta);
      }
      tableMeta.addColumn(columnMeta);
    });
    return tableMeta;
  }
}
