package github.koala.rpc;

import com.google.common.base.Strings;
import github.koala.rpc.message.RpcRequestProtocol;
import github.koala.rpc.message.RpcResponseProtocol;
import github.koala.rpc.message.RpcServiceClient;
import github.koala.rpc.zookeeper.ServiceEventHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

@Slf4j
public class RpcProxyObject implements MethodInterceptor, ServiceEventHandler {

  Class implClass;
  KoalaRpcRegistry registry;

  RpcServiceClient client;
  Boolean isServiceOnline = false;

   <T> RpcProxyObject(Class<T> implClass, KoalaRpcRegistry koalaRpcRegistry) {
    this.implClass = implClass;
    this.registry = koalaRpcRegistry;
    fetchServiceThread();
    registry.registeHandler(this);
  }

  private Boolean fetchService() {
    log.info("Fetching service :{} ", implClass.getName());

    String remoteServiceUrl = registry.getServiceServerURL(implClass);
    if (!Strings.isNullOrEmpty(remoteServiceUrl)) {
      log.info("connecting service :{} ", implClass.getName());
      client = RpcServiceClient.addClient(remoteServiceUrl);
      isServiceOnline = true;
    }
    return isServiceOnline;
  }

  private void fetchServiceThread() {
    Thread fetchService = new Thread(() -> {
      while (!isServiceOnline) {
        try {
          fetchService();
          Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    fetchService.start();
  }

  @Override
  public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy)
      throws Throwable {

    Instant start = Instant.now();

    //屏蔽toString ,equals等方法
    Object parentMethodResult = checkProxyMethod(method, args);
    if (parentMethodResult != null) {
      return parentMethodResult;
    }

    log.info("----------------------------------------------");
    log.info("[{}]调用远程方法[{}].", implClass.getSimpleName(), method.getName());

    if (!isServiceOnline && !fetchService()) {
      log.info("远程服务未注册 ,请等待");
      return null;
    }

    if (!client.isConnected()) {
      log.info("远程服务已断线 ,正在重连");
      client.reconnect();
    }

    RpcRequestProtocol protocol = new RpcRequestProtocol(implClass.getName(), method.getName(),
        Arrays.asList(args));
    String result = client.send(protocol.serialization());
    RpcResponseProtocol rpcResponseProtocol = RpcResponseProtocol.deserialization(result);
    Object resultObj = rpcResponseProtocol.deserializationResultObject(method.getReturnType());

    log.info("[{}]远程方法[{}]代理执行时间:[{}]", implClass.getName(), method.getName(),
        Duration.between(start, Instant.now()).toMillis());
    log.info("----------------------------------------------\n");

    return resultObj;
  }

  /**
   * 代理接口中属于Object的方法
   */
  private Object checkProxyMethod(Method method, Object[] args) {
    if (method.getName().equals("toString")) {
      return super.toString();
    } else if (method.getName().equals("equals")) {
      return super.equals(args[0]);
    }
    return null;
  }

  @Override
  public void handleServiceDelete() {
    isServiceOnline = false;
    fetchServiceThread();
  }
}
