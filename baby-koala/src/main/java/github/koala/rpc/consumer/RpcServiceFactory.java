package github.koala.rpc.consumer;

import github.koala.rpc.consumer.RpcProxyObject;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author edliao on 2017/7/6.
 * @description RPC服务提供中心
 */
@Slf4j
public class RpcServiceFactory {

  private RpcServiceFactory() {
  }

  public static Object getProxyInstance(Class classType) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(classType);
    enhancer.setCallback(new RpcProxyObject(classType));
    return enhancer.create();
  }

}
