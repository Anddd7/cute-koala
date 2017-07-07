package github.koala.rpc;

import github.and777.common.CollectionTool;
import github.koala.core.factory.KoalaFactory;
import github.koala.rpc.provider.RpcServiceServer;
import github.koala.zookeeper.AbstractServiceRegistry;
import github.koala.zookeeper.KoalaWatcher;
import github.koala.zookeeper.config.KoalaForestConfig;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description RPC服务注册中心
 */
@Slf4j
public class RpcServiceRegistry extends AbstractServiceRegistry {

  protected static final String RPC_PATH = SERVICE_PATH + "/rpc";

  RpcServiceServer serviceServer;
  KoalaFactory factory;

  private RpcServiceRegistry(KoalaWatcher watcher) {
    super(watcher);
    try {
      watcher.mkdirs(RPC_PATH);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 创建一个单例的RPC服务注册中心
   */
  public static RpcServiceRegistry initRegistry(KoalaFactory factory,
      RpcServiceServer serviceServer) {
    rpcRegistry.serviceServer = serviceServer;
    rpcRegistry.factory = factory;
    return rpcRegistry;
  }


  private static RpcServiceRegistry rpcRegistry = new RpcServiceRegistry(
      new KoalaWatcher(KoalaForestConfig.getRpcConfig(), new RpcEventHandler()));

  public static RpcServiceRegistry getRegistry() {
    return rpcRegistry;
  }

  /**
   * Rpc
   */
  public void addService(Class defineType) {
    String path = RPC_PATH + "/" + defineType.getName();
    String connectString = serviceServer.getConnectString();
    log.info("发布服务{}到{}", path, connectString);

    addService(path, connectString);
  }

  public String getService(Class defineType) {
    return getService(RPC_PATH + "/" + defineType.getName());
  }

  public RpcResponseProtocol executeService(RpcRequestProtocol rpcProtocol) {
    RpcResponseProtocol result = new RpcResponseProtocol();
    try {
      Class classType = Class.forName(rpcProtocol.getServiceName());
      Object service = factory.getBean(classType);

      log.info("获取到目标Service:{}", service);

      Optional<Method> method = CollectionTool.getFirst(
          classType.getDeclaredMethods(),
          method1 -> method1.getName().equals(rpcProtocol.getMethodName()));

      if (method.isPresent()) {
        log.info("获取到目标方法:{}", method.get().getName());
        Method m = method.get();
        List<Object> parameters = rpcProtocol.deserializationParameters(m.getParameterTypes());

        result.setResultType(m.getReturnType());
        result.setResultObject(m.invoke(service, parameters.toArray()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }
}
