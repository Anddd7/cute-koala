package others.socket.tcp;

import others.socket.LocalComputer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/15.
 * @description NIO的Socket
 *
 * 切换线程有代价 ,过多的线程也会影响效率 如果大多数时间都在等待IO ,线程的效率就会很低 (CPU不会等待)
 */
@Slf4j
public class NIOSocketServer extends LocalComputer {

  private static final int BUFSIZE = 1024;


  public static void main(String[] a) throws IOException {
    NIOSocketServer socketServer = new NIOSocketServer();
    socketServer.start();
  }

  @Override
  public void start() throws IOException {
    //创建一个选择器 ,可以监听多个信道
    Selector selector = Selector.open();

    for (int port : PORTs) {
      ServerSocketChannel listnChannel = ServerSocketChannel.open();
      listnChannel.socket().bind(new InetSocketAddress(port));
      listnChannel.configureBlocking(false);
      listnChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    //不断轮询select方法，获取准备好的信道所关联的Key集
    while (true) {

      //select是唯一的阻塞方法 ,类似socket的accept
      //一旦准备好 ,后续的操作都可以直接getchannel然后立即读写
      if (selector.select(3000) == 0) {
        System.out.print(".");
        continue;
      }
      System.out.println();
      log.debug("等待新信道");

      //获取准备好的信道所关联的Key集合的iterator实例
      Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
      //循环取得集合中的每个键值
      while (keyIter.hasNext()) {
        log.debug("处理信道操作");
        SelectionKey key = keyIter.next();
        if (key.isAcceptable()) {
          handleAccept(key);
        }
        if (key.isReadable()) {
          handleRead(key);
        }
        if (key.isValid() && key.isWritable()) {
          handleWrite(key);
        }
        //这里需要手动从键集中移除当前的key
        keyIter.remove();
        log.debug("信道操作处理完毕");
      }
    }
  }

  /**
   * NIO的 accept/read/write 都是非阻塞 ,马上可以完成的(与channel交互)
   * 所以一旦确定channel当前可以accept/read/write ,就说明连接已经到来/数据已经在channel中/数据可以写入channel
   *
   * NIO情况下
   * handle函数是有实际作用的 不会等待IO
   * 等待的过程交给select的线程即可
   */
  private void handleWrite(SelectionKey key) throws IOException {
    log.debug("Write 处理中,isAcceptable:{} isReadable:{} isWritable:{} isValid:{}",
        key.isAcceptable(), key.isReadable(), key.isWritable(), key.isValid());

    //获取与该信道关联的缓冲区，里面有之前读取到的数据
    ByteBuffer buf = (ByteBuffer) key.attachment();
    buf.flip();

    ByteBuffer feedback = ByteBuffer.allocate(1024);
    feedback.put("RE:".getBytes());
    feedback.put(buf);
    feedback.flip();

    SocketChannel socketChannel = (SocketChannel) key.channel();
    socketChannel.write(feedback);
    if (!feedback.hasRemaining()) {
      key.interestOps(SelectionKey.OP_READ);
    }
    //为读入更多的数据腾出空间
    buf.compact();
  }

  private void handleRead(SelectionKey key) throws IOException {
    log.debug("Read 处理中,isAcceptable:{} isReadable:{} isWritable:{} isValid:{}",
        key.isAcceptable(), key.isReadable(), key.isWritable(), key.isValid());

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
    log.debug("Accept 处理中,isAcceptable:{} isReadable:{} isWritable:{} isValid:{}",
        key.isAcceptable(), key.isReadable(), key.isWritable(), key.isValid());

    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
    socketChannel.configureBlocking(false);
    socketChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(BUFSIZE));
  }


}
