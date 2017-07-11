package github.koala.orm;

import github.koala.orm.conn.DBConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author edliao on 2017/7/7.
 * @description
 */
public class DataBasePool {

  private static DataBasePool pool = new DataBasePool();

  public static DBConnection getDBConnectionBySchema(String schema) {
    return pool.connectionMap.get(schema);
  }

  public static void addDBConnection(DBConnection dbConnection) {
    pool.connectionMap.put(dbConnection.getSchema(), dbConnection);
  }


  //Pool
  Map<String, DBConnection> connectionMap = new ConcurrentHashMap<>();
}
