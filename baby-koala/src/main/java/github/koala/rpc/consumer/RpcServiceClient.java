package github.koala.rpc.consumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
@Slf4j
public class RpcServiceClient {

  public static final Map<String, RpcServiceClient> clients = new HashMap<>();

  public static RpcServiceClient addClient(String url) {
    return clients.put(url, new RpcServiceClient(url));
  }

  public static String send(String url, String bytes) {
    return clients.get(url).send(bytes);
  }

  SocketChannel socketChannel;
  InetSocketAddress address;

  RpcServiceClient(String url) {
    try {
      log.info("创建NIO连接:{}",url);
      String[] hostAndPort = url.split(":");
      address = new InetSocketAddress(hostAndPort[0], Integer.valueOf(hostAndPort[1]));

      //阻塞模式
      socketChannel = SocketChannel.open();
      socketChannel.connect(address);
      socketChannel.finishConnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void reconnect() {
    try {
      socketChannel.connect(address);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  public boolean isConnected() {
    return socketChannel.isConnected();
  }


  public String send(String msg) {
    String result = null;

    log.debug("开始发送数据");
    try {
      ByteBuffer writeBuf = ByteBuffer.wrap(msg.getBytes());
      socketChannel.write(writeBuf);

      ByteBuffer readBuf = ByteBuffer.allocate(1024);
      if (socketChannel.read(readBuf) > 0) {
        result = new String(readBuf.array());
        log.debug("获取来自服务器的信息:" + result);
        readBuf.clear();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

}