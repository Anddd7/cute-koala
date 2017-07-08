package github.koala.orm.conn;

import com.mysql.cj.core.result.Field;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/7.
 * @description 数据库连接
 */
@Slf4j
public class MysqlConnection implements DBConnection {

  final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  String url;
  String username;
  String password;

  List<String> urlParameters = new ArrayList<>();

  Connection conn = null;
  private ResultSetMetaData metaData;

  public MysqlConnection(String url, String username, String password) {
    this(url, username, password, Arrays.asList("serverTimezone=UTC"));
  }

  public MysqlConnection(String url, String username, String password, List<String> urlParameters) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.urlParameters = urlParameters;
    try {
      Class.forName(JDBC_DRIVER);
      String dbUrl = formatDBUrl();
      log.info("连接数据库{}",dbUrl);
      conn = DriverManager.getConnection(dbUrl, username, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  String formatDBUrl() {
    StringBuilder sb = new StringBuilder("jdbc:mysql://").append(url);
    if (!urlParameters.isEmpty()) {
      sb.append("?").append(String.join("$", urlParameters));
    }
    return sb.toString();
  }


  @Override
  public Field[] fetchField(String tableName) {
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = conn.createStatement();
      rs = statement.executeQuery("select * from "+tableName+" limit 0,0");

      ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();
      return metaData.getFields();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeRsAndStmt(rs, statement);
    }
    return null;
  }

  @Override
  public Object executeQuery(Method method) {
    return null;
  }

  @Override
  public int execute() {
    return 0;
  }
}
