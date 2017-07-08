package github.koala.orm.util;

import com.mysql.cj.core.result.Field;
import github.eddy.common.TemplateGenerator;
import github.koala.orm.conn.DBConnection;
import github.koala.orm.util.meta.ColumnMeta;
import github.koala.orm.util.meta.TableMeta;
import github.koala.orm.util.template.Temp4Implement;
import github.koala.orm.util.template.Temp4Interface;
import github.koala.orm.util.template.Temp4POJO;
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

  public void generate(String sourcePath,String packagePath,String tableName) {
    TableMeta tableMeta = generateMeta(tableName);

    TemplateGenerator generator = new TemplateGenerator();

    Temp4POJO.from(sourcePath ,packagePath,tableMeta).generateTo(generator);
    Temp4Interface.from(sourcePath ,packagePath,tableMeta).generateTo(generator);
    Temp4Implement.from(sourcePath ,packagePath,tableMeta).generateTo(generator);
  }

  TableMeta generateMeta(String tableName) {
    TableMeta tableMeta = new TableMeta();
    tableMeta.setTableName(tableName);

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
      }
      tableMeta.addColumn(columnMeta);
    });
    return tableMeta;
  }
}
