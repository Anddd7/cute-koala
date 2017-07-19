package github.koala.orm.conn;

import github.koala.orm.util.meta.TableMeta;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author edliao on 2017/7/7.
 * @description 连接接口
 */
public interface DBConnection {

  TableMeta fetchField(String tableName);

  <T> T executeQuery(String sql, List<Object> attrs, Class<T> pojoClass);

  <T, C> C executeQuery(String sql, List<Object> attrs, Class<T> pojoClass,
      Class<C> collectionType);

  int executeUpdate(String sql);

  int executeUpdate(String sql, List<Object> attrs);

  String getHostAndPort();

  String getSchema();

  String getConnectUrl();

  default void closeRsAndStmt(ResultSet rs, Statement statement) {
    try {
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeStmt(statement);
  }

  default void closeStmt(Statement statement) {
    try {
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
