package github.koala.core.socket;

import java.io.IOException;

/**
 * @author edliao on 2017/6/15.
 * @description 服务器客户端的抽象集合
 */
public abstract class LocalComputer {

  public static final String IP = "127.0.0.1";
  public static final int PORT = 20006;

  public static final int TIMEOUT = 0;


  public static final int SERVER_PORT = 19001;
  public static final  int CLIENT_PORT = 19002;

  public static final int[] PORTs = {20006,20007};

  public abstract void start() throws IOException;
}
