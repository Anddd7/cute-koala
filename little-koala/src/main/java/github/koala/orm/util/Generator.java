package github.koala.orm.util;

import github.koala.orm.conn.DBConnection;
import github.koala.orm.util.meta.TableMeta;
import github.koala.orm.util.template.Template4ORM;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Generator {

  DBConnection connection;

  public Generator(DBConnection connection) {
    this.connection = connection;
  }

  public void generate(String sourcePath, String packagePath, String... tableNames) {
    for (String tableName : tableNames) {
      generate(sourcePath, packagePath, tableName);
    }
  }

  public void generate(String sourcePath, String packagePath, String tableName) {
    log.info("ORM : Generate ORM .java file for {}", tableName);
    TableMeta tableMeta = connection.fetchField(tableName);
    Template4ORM.from(sourcePath, packagePath, tableMeta).generateTo();
  }
}
