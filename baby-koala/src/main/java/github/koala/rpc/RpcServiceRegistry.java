package github.koala.rpc;

import github.koala.rpc.provider.RpcServiceServer;
import github.koala.zookeeper.AbstractServiceRegistry;
import github.koala.zookeeper.KoalaWatcher;
import github.koala.zookeeper.config.KoalaForestConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description RPC服务注册中心
 */
@Slf4j
public class RpcServiceRegistry extends AbstractServiceRegistry {

  protected static final String RPC_PATH = SERVICE_PATH + "/rpc";

  RpcServiceServer serviceServer;

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
  public static RpcServiceRegistry initRegistry(RpcServiceServer serviceServer) {
    rpcRegistry.serviceServer = serviceServer;
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
}
