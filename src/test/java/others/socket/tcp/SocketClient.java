package others.socket.tcp;

import others.socket.LocalComputer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description 客户端
 */
@Slf4j
public class SocketClient extends LocalComputer {

  Socket client;

  /**
   * 发送信息 线程
   */
  public Runnable getSend() {
    return () -> {
      try {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(client.getOutputStream());

        while (true) {
          String str = input.readLine();
          log.debug("本地发送信息:" + str);
          out.println(str);

          if ("esc".equals(str)) {
            break;
          }
        }
        shutdown();
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  /**
   * 关闭Client
   */
  private void shutdown() throws IOException {
    if (client != null) {
      client.close();
    }
  }

  /**
   * 接受信息线程
   */
  public Runnable getReceive() {
    return () -> {
      try {
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (!client.isClosed()) {
          log.debug("等待服务器消息");
          String str = buf.readLine();
          log.debug("获取来自服务器的信息:" + str);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  @Override
  public void start() throws IOException {
    client = new Socket(IP, PORT);
    client.setSoTimeout(TIMEOUT);

    Thread send = new Thread(getSend());
    Thread receive = new Thread(getReceive());

    send.start();
    receive.start();

    try {
      send.join();
      receive.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] a) throws IOException {
    SocketClient socketClient = new SocketClient();
    socketClient.start();
  }
}
