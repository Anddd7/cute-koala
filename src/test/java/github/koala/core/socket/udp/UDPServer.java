package github.koala.core.socket.udp;

import github.koala.core.socket.LocalComputer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description 服务器
 */
@Slf4j
public class UDPServer extends LocalComputer {

  private static final int BUFFER_SIZE = 1024;

  DatagramSocket datagramSocket;
  InetAddress remoteAddress;

  boolean run = true;

  public static void main(String args[]) throws IOException {
    UDPServer server = new UDPServer();
    server.start();
  }


  public Runnable service() {
    return () -> {
      byte[] buf = new byte[BUFFER_SIZE];
      DatagramPacket data = new DatagramPacket(buf, BUFFER_SIZE);

      while (run) {
        try {
          log.debug("等待客户端消息");
          datagramSocket.receive(data);
          if (!data.getAddress().equals(remoteAddress)) {
            run = false;
            throw new IOException();
          }
          log.debug("接收到客户端的消息:" + new String(buf));
          data.setLength(BUFFER_SIZE);

          String message = "RE:" + new String(buf);
          DatagramPacket feedback = new DatagramPacket(message.getBytes(), message.length(),
              remoteAddress,
              CLIENT_PORT);
          datagramSocket.send(feedback);
          log.debug("返回消息给客户端");

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  @Override
  public void start() throws IOException {
    datagramSocket = new DatagramSocket(SERVER_PORT);
    datagramSocket.setSoTimeout(TIMEOUT);
    remoteAddress = InetAddress.getLocalHost();

    Thread service = new Thread(service());
    service.start();

    try {
      service.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
