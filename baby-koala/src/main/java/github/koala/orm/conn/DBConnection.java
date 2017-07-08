package github.koala.orm.conn;

import com.mysql.cj.core.result.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author edliao on 2017/7/7.
 * @description 连接接口
 */
public interface DBConnection {

  Field[] fetchField(String tableName);

  Object executeQuery(Method method);

  int execute();

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
