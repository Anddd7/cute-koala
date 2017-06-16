package github.koala.core.socket.tcp;

import com.google.common.collect.Lists;
import github.koala.core.socket.LocalComputer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description IO Socket
 */
@Slf4j
public class SocketServer extends LocalComputer {

  ServerSocket serverSocket;
  //存储活动的socket列表
  List<SocketWrapper> socketWappers = Lists.newArrayList();

  /**
   * 为socket赋予一个名称
   */
  class SocketWrapper {


    public String name;
    public Socket socket;

    public SocketWrapper(String name, Socket socket) {
      this.name = name;
      this.socket = socket;
    }
  }

  /**
   * 对客户端的响应
   */
  public Runnable service(Socket client) {
    return () -> {
      try {
        String name = Thread.currentThread().getName();

        log.debug("开启新连接:" + name + ", Timeout :" + client.getSoTimeout());
        socketWappers.add(new SocketWrapper(Thread.currentThread().getName(), client));

        PrintStream out = new PrintStream(client.getOutputStream());
        out.println("欢迎连接本服务器:" + IP + ":" + PORT);

        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (!client.isClosed()) {
          log.debug("等待客户端消息");
          String str = buf.readLine();
          log.debug("接受来自客户端{}的消息:" + str, name);
          if ("bye".equals(str)) {
            client.close();
          } else {
            out.println("echo:" + str);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  /**
   * 检查连接是否可用
   */
  public Runnable check() {
    return () -> {
      while (true) {
        try {
          log.debug("检测Client状态中~");
          Thread.sleep(10 * 1000);
          socketWappers.removeIf(wrapper -> {
            boolean notConnected = !wrapper.socket.isConnected() || wrapper.socket.isClosed();
            log.debug("Socket-{} connected ? {} closed ? {}", wrapper.name,
                wrapper.socket.isConnected(), wrapper.socket.isClosed());
            return notConnected;
          });
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
  }

  @Override
  public void start() throws IOException {
    serverSocket = new ServerSocket(PORT);

    new Thread(check()).start();

    Executor service = Executors.newFixedThreadPool(1);

    while (!serverSocket.isClosed()) {
      //线程池运行
      service.execute(service(serverSocket.accept()));
      //new Thread(service(serverSocket.accept())).start();
    }
  }

  public static void main(String[] a) throws IOException {
    SocketServer socketServer = new SocketServer();
    socketServer.start();
  }

}
