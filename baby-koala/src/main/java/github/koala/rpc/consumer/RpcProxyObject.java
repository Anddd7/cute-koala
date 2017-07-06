package github.koala.rpc.consumer;

import com.google.common.base.Strings;
import github.koala.rpc.RpcProtocol;
import github.koala.rpc.RpcServiceRegistry;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
@Slf4j
public class RpcProxyObject implements MethodInterceptor {

  Class classType;
  @Setter
  String rpcUrl;
  private String className;

  RpcServiceClient rpcServiceClient;


  Boolean isServiceOnline;

  public RpcProxyObject(Class classType) {
    this.classType = classType;
    this.className = classType.getName();
    fetchService();
  }

  public void fetchService() {
    this.rpcUrl = RpcServiceRegistry.getRegistry().getService(classType);
    this.rpcServiceClient = RpcServiceClient.addClient(rpcUrl);
    this.isServiceOnline = !Strings.isNullOrEmpty(rpcUrl);
    log.info("获取目标服务{}地址{}", className,rpcUrl);
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
    log.info("[{}]调用远程方法[{}].", className, method.getName());

    if (!isServiceOnline) {
      log.info("远程服务未注册 ,请等待");
      fetchService();
    }

    if (!rpcServiceClient.isConnected()) {
      log.info("远程服务已断线 ,正在重连");
      rpcServiceClient.reconnect();
    }

    RpcProtocol protocol = new RpcProtocol(className, method.getName(), Arrays.asList(args));
    String result = rpcServiceClient.send(protocol.serialization());

    log.info("[{}]远程方法[{}]代理执行时间:[{}]", className, method.getName(),
        Duration.between(start, Instant.now()).toMillis());
    log.info("----------------------------------------------\n");
    return result;
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
}
