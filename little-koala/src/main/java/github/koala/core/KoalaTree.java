package github.koala.core;

import github.koala.bean.KoalaLocalRegistry;
import github.koala.bean.pool.KoalaZoo;
import github.koala.rpc.KoalaRpcRegistry;
import github.koala.rpc.message.RpcServiceServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KoalaTree {

  /**
   * //the singleton in application
   * TODO test
   */
  public static KoalaTree of(int port, Class moduleClass) {
    return new KoalaTree().init(port, moduleClass);
  }

  public static KoalaTree of(Class moduleClass) {
    return new KoalaTree().init(-1, moduleClass);
  }

  /**
   * scan module and add bean into beanPool
   */
  private final KoalaZoo beanPool = new KoalaZoo();
  private final KoalaScanner scanner = new KoalaScanner();

  private KoalaTree init(int port, Class moduleClass) {
    scanner.addRegistry(new KoalaLocalRegistry(beanPool));
    if (port > 0) {
      scanner.addRegistry(
          new KoalaRpcRegistry(beanPool, new RpcServiceServer(port, this)));
    }
    scanner.findKoala(moduleClass);
    return this;
  }

  /**
   * get Koala to outter
   */
  public <T> T getKoala(Class<T> classType) {
    return beanPool.getKoala(classType);
  }
}
