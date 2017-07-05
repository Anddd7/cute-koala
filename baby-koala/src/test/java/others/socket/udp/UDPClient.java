package others.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import lombok.extern.slf4j.Slf4j;
import others.socket.LocalComputer;

/**
 * @author edliao on 2017/6/15.
 * @description UDP
 */
@Slf4j
public class UDPClient extends LocalComputer {

  private static final int BUFFER_SIZE = 1024;

  DatagramSocket datagramSocket;
  InetAddress remoteAddress;

  boolean run = true;

  public static void main(String args[]) throws IOException {
    UDPClient client = new UDPClient();
    client.start();
  }


  public Runnable getSend() {
    return () -> {
      while (run) {
        try {
          log.debug("发送消息");
          String message = "Hello UDP server";
          DatagramPacket data = new DatagramPacket(message.getBytes(), message.length(),
              remoteAddress,
              SERVER_PORT);
          datagramSocket.send(data);
          Thread.sleep(10 * 1000);

        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
  }

  public Runnable getReceive() {
    return () -> {
      byte[] buf = new byte[BUFFER_SIZE];
      DatagramPacket data = new DatagramPacket(buf, BUFFER_SIZE);

      while (run) {
        try {
          log.debug("等待服务器消息");
          datagramSocket.receive(data);
          if (!data.getAddress().equals(remoteAddress)) {
            run = false;
            throw new IOException();
          }
          log.debug("接收到服务器的消息:" + new String(buf));
          data.setLength(BUFFER_SIZE);

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  @Override
  public void start() throws IOException {
    datagramSocket = new DatagramSocket(CLIENT_PORT);
    datagramSocket.setSoTimeout(TIMEOUT);
    remoteAddress = InetAddress.getLocalHost();

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
}

