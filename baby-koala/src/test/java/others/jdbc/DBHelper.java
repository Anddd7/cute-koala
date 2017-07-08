package others.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
public class DBHelper {

  // JDBC 驱动名及数据库 URL
  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
  static final String DB_URL = "jdbc:mysql://localhost:3306/sakila?serverTimezone=UTC&characterEncoding=utf-8";

  // 数据库的用户名与密码，需要根据自己的设置
  static final String USER = "root";
  static final String PASS = "root";

  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;
    try {
      // 注册 JDBC 驱动
      Class.forName("com.mysql.jdbc.Driver");

      // 打开链接
      System.out.println("连接数据库...");
      conn = DriverManager.getConnection(DB_URL, USER, PASS);

      // 执行查询
      System.out.println(" 实例化Statement对...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT * FROM actor";
      ResultSet rs = stmt.executeQuery(sql);
      ResultSetMetaData metaData = rs.getMetaData();
      // 展开结果集数据库
      while (rs.next()) {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          System.out.println(
              String.format("%s,%s,%s,%s,%s,%s,%s",
                  metaData.getColumnName(i),
                  metaData.getColumnType(i),
                  metaData.getColumnClassName(i),
                  metaData.getColumnLabel(i),
                  metaData.getColumnDisplaySize(i),
                  metaData.getTableName(i), metaData.getSchemaName(i))
          );
          System.out.println(rs.getObject(i));
        }
      }
      // 完成后关闭
      rs.close();
      stmt.close();
      conn.close();
    } catch (SQLException se) {
      // 处理 JDBC 错误
      se.printStackTrace();
    } catch (Exception e) {
      // 处理 Class.forName 错误
      e.printStackTrace();
    } finally {
      // 关闭资源
      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (SQLException se2) {
      }// 什么都不做
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
    System.out.println("Goodbye!");
  }
}
