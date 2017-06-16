package github.koala.core.socket.tcp;

import github.koala.core.socket.LocalComputer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description
 */
@Slf4j
public class NIOSocketClient extends LocalComputer {

  SocketChannel socketChannel;

  public static void main(String[] a) throws IOException {
    NIOSocketClient socketClient = new NIOSocketClient();
    socketClient.start();
  }


  /**
   * 发送信息 线程
   */
  public Runnable getSend() {
    return () -> {
      try {
        String message = "NIO Socket Connect from Client.";
        while (true) {
          ByteBuffer writeBuf = ByteBuffer.wrap(message.getBytes());
          log.debug("开始发送数据");

          while (true) {
            //异步写 ,结束后跳出
            if (writeBuf.hasRemaining()) {
              socketChannel.write(writeBuf);
            } else {
              break;
            }
          }

          log.debug("数据发送完毕");
          Thread.sleep(30 * 1000);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }

  /**
   * 接受信息线程
   */
  public Runnable getReceive() {
    return () -> {
      try {
        ByteBuffer readBuf = ByteBuffer.allocate(1024);
        log.debug("等待服务器消息");
        while (true) {
          //读取数据
          if (socketChannel.read(readBuf) > 0) {
            log.debug("获取来自服务器的信息:" + new String(readBuf.array()));
            readBuf.clear();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  @Override
  public void start() throws IOException {
    //创建一个信道，并设为非阻塞模式
    socketChannel = SocketChannel.open();
    socketChannel.configureBlocking(false);

    if (!socketChannel.connect(new InetSocketAddress(IP, PORT))) {
      log.debug("正在连接.");
      while (!socketChannel.finishConnect()) {
        //等待连接时可以执行其他任务
        System.out.print(".");
      }
      System.out.println();
      log.debug("连接成功.");
    }

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
