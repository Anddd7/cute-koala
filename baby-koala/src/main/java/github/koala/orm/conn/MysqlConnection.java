package github.koala.orm.conn;

import com.mysql.cj.core.result.Field;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/7.
 * @description 数据库连接
 */
@Slf4j
public class MysqlConnection implements DBConnection {

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

  String hostAndPort;
  String schema;
  List<String> options = new ArrayList<>();

  Connection conn = null;

  public MysqlConnection(String hostAndPort, String schema, String username, String password) {
    this(hostAndPort, schema, username, password, Arrays.asList("serverTimezone=UTC"));
  }

  public MysqlConnection(String hostAndPort, String schema, String username, String password,
      List<String> options) {
    this.hostAndPort = hostAndPort;
    this.schema = schema;
    this.options = options;
    try {
      Class.forName(JDBC_DRIVER);
      String dbUrl = getConnectUrl();
      log.info("连接数据库{}", dbUrl);
      conn = DriverManager.getConnection(dbUrl, username, password);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Field[] fetchField(String tableName) {
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = conn.createStatement();
      rs = statement.executeQuery("select * from " + tableName + " limit 0,0");

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
  public <T> T executeQuery(String sql, List<Object> attrs, Class<T> pojoClass) {
    return executeQuery(sql, attrs, pojoClass, pojoClass);
  }

  @Override
  public <T, C> C executeQuery(String sql, List<Object> attrs, Class<T> pojoClass,
      Class<C> collectionType) {
    System.out.println("SQL : "+sql);
    System.out.println("ATTRS : "+attrs);

    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = conn.prepareStatement(sql);
      if (!Objects.isNull(attrs)) {
        for (int i = 0; i < attrs.size(); i++) {
          statement.setObject(i+1, attrs.get(i));
        }
      }
      rs = statement.executeQuery();

      List<T> result = new ArrayList<>();
      while (rs.next()) {
        T pojo = pojoClass.newInstance();
        java.lang.reflect.Field[] fields = pojoClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
          fields[i].setAccessible(true);
          fields[i].set(pojo, rs.getObject(i+1));
        }

        if (collectionType.equals(pojoClass)) {
          return (C) pojo;
        }
        result.add(pojo);
      }
      return (C) result;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeRsAndStmt(rs, statement);
    }
    return null;
  }

  @Override
  public int executeUpdate(String sql) {
    return executeUpdate(sql, null);
  }

  @Override
  public int executeUpdate(String sql, List<Object> attrs) {
    System.out.println("SQL : "+sql);
    System.out.println("ATTRS : "+attrs);

    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = conn.prepareStatement(sql);
      if (!Objects.isNull(attrs)) {
        for (int i = 0; i < attrs.size(); i++) {
          statement.setObject(i, attrs.get(i));
        }
      }
      return statement.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeRsAndStmt(rs, statement);
    }
    return -1;
  }

  @Override
  public String getHostAndPort() {
    return hostAndPort;
  }

  @Override
  public String getSchema() {
    return schema;
  }

  @Override
  public String getConnectUrl() {
    StringBuilder sb = new StringBuilder("jdbc:mysql://").append(hostAndPort).append("/")
        .append(schema);
    if (!options.isEmpty()) {
      sb.append("?").append(String.join("$", options));
    }
    return sb.toString();
  }
}
