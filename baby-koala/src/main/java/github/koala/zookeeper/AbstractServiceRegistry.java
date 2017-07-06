package github.koala.zookeeper;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public abstract class AbstractServiceRegistry {

  KoalaWatcher watcher;

  protected AbstractServiceRegistry(KoalaWatcher watcher) {
    this.watcher = watcher;
  }

  protected static final String SERVICE_PATH = "/koala";

  protected void addService(String servicePath, String serviceDesc) {
    watcher.setData(servicePath, serviceDesc.getBytes());
  }

  protected String getService(String servicePath) {
    byte[] bytes = watcher.getData(servicePath);
    return bytes != null ? new String(bytes) : null;
  }

  /**
   * 默认非持久化节点 ,服务器掉线即删除
   */
  public void deleteService(String servicePath) {

  }

}
