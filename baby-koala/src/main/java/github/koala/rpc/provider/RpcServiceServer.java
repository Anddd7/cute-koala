package github.koala.rpc.provider;

import github.koala.core.factory.KoalaFactory;
import github.koala.core.factory.pool.BeanPool;
import github.koala.rpc.RpcProtocol;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
@Slf4j
public class RpcServiceServer {

  ServerSocketChannel channel;
  Selector selector;

  InetSocketAddress address;

  Read read;

  public RpcServiceServer(Integer port) {
    try {
      address = new InetSocketAddress(port);
      channel = ServerSocketChannel.open();
      channel.configureBlocking(false);
      channel.socket().bind(address);

      selector = Selector.open();
      channel.register(selector, SelectionKey.OP_ACCEPT);
    } catch (IOException ex) {
      System.out.println("Couldn't setup server socket");
      System.out.println(ex.getMessage());
      System.exit(1);
    }

    read = new Read(selector);
    read.start();
  }


  public String getConnectString() {
    return address.getHostString() + ":" + address.getPort();
  }

  class Read extends Thread {

    Selector selector;

    public Read(Selector selector) {
      this.selector = selector;
    }

    public void run() {
      //轮训
      while (true) {
        try {
          //如果接收到请求
          while (this.selector.select() > 0) {
            Iterator<SelectionKey> selectionKeyIterator = this.selector.selectedKeys().iterator();

            //处理接收到的请求...
            while (selectionKeyIterator.hasNext()) {
              SelectionKey key = selectionKeyIterator.next();

              if (key.isAcceptable()) {
                handleAccept(key);
              } else if (key.isReadable()) {
                handleRead(key);
              } else if (key.isWritable()) {
                handleWrite(key);
              }
              selectionKeyIterator.remove();
            }
          }
        } catch (IOException ex) {
          System.out.println("Error in poll loop");
          System.out.println(ex.getMessage());
          System.exit(1);
        }
      }
    }

    private void handleWrite(SelectionKey key) throws IOException {
      log.debug("Write 处理中");

      //获取与该信道关联的缓冲区，里面有之前读取到的数据
      ByteBuffer buf = (ByteBuffer) key.attachment();
      buf.flip();

      ByteBuffer feedback = ByteBuffer.allocate(1024);
      feedback.put(RpcServiceServer.executeRPC(buf));
      feedback.flip();

      SocketChannel socketChannel = (SocketChannel) key.channel();
      socketChannel.write(feedback);
      key.interestOps(SelectionKey.OP_READ);

      //为读入更多的数据腾出空间
      buf.compact();
    }

    private void handleRead(SelectionKey key) throws IOException {
      log.debug("Read 处理中");

      SocketChannel socketChannel = (SocketChannel) key.channel();
      ByteBuffer buf = (ByteBuffer) key.attachment();
      long bytesRead = socketChannel.read(buf);
      if (bytesRead == -1) {
        socketChannel.close();
      } else if (bytesRead > 0) {
        //如果缓冲区总读入了数据，则将该信道感兴趣的操作设置为为可读可写
        key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      }
    }

    private void handleAccept(SelectionKey key) throws IOException {
      log.debug("Accept 处理中");
      SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
    }
  }

  /**
   * 执行RPC方法
   */
  public static byte[] executeRPC(ByteBuffer buf) throws CharacterCodingException {
    Charset charset = Charset.forName("UTF-8");
    CharsetDecoder decoder = charset.newDecoder();
    CharBuffer charBuffer = decoder.decode(buf);
    String msg = charBuffer.toString();

    RpcProtocol rpcProtocol = RpcProtocol.deserialization(msg);







    return null;
  }

}

