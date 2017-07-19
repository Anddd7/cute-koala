package github.koala.rpc;

import github.eddy.common.ReflectTool;
import github.koala.annotation.KoalaExport;
import github.koala.annotation.KoalaImport;
import github.koala.bean.Koala;
import github.koala.bean.pool.KoalaZoo;
import github.koala.core.AbstractRegistry;
import github.koala.rpc.message.RpcServiceServer;
import github.koala.rpc.zookeeper.KoalaWatcher;
import java.lang.reflect.Field;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;

@Slf4j
public class KoalaRpcRegistry extends AbstractRegistry {

  public KoalaRpcRegistry(KoalaZoo zoo, RpcServiceServer serviceServer) {
    super(zoo);
    this.serviceServer = serviceServer;
    this.watcher = new KoalaWatcher();
  }

  @Override
  public Object createInstance(Class implClass) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(implClass);
    enhancer.setCallback(new RpcProxyObject(implClass, this));
    return enhancer.create();
  }

  @Override
  public Class registerKoala(Field field) {
    if (!Objects.isNull(field.getAnnotation(KoalaExport.class))) {
      //通过zookeeper发布出去
      addServiceServerURL(field.getType());
    } else if (!Objects.isNull(field.getAnnotation(KoalaImport.class))) {
      Koala proxyKoala = zoo.get(field.getType());
      if (proxyKoala == null) {
        //创建代理对象 ,加入zoo
        log.info("Create import-proxy object of {}", field.getType());
        Class defineType = field.getType();
        proxyKoala = new Koala(defineType, true, this);
        zoo.put(defineType, proxyKoala);
      }

      Class parent = field.getDeclaringClass();
      ReflectTool.setFieldOfObject(field, zoo.getKoala(parent), proxyKoala.getObject());
      log.info("Set import-proxy object {} into {}:{}",
          proxyKoala.getImplementClazz().getSimpleName(),
          parent.getName(),
          field.getName());
    }

    return null;
  }

  /**
   * get service
   */
  KoalaWatcher watcher;
  RpcServiceServer serviceServer;

  protected void addServiceServerURL(Class defineType) {
    watcher.setData(
        KoalaWatcher.ROOT_PATH + "/rpc/" + defineType.getName(),
        serviceServer.getConnectString().getBytes());
  }

  protected String getServiceServerURL(Class defineType) {
    String path = KoalaWatcher.ROOT_PATH + "/rpc/" + defineType.getName();
    byte[] bytes = watcher.getData(path);
    return bytes != null ? new String(bytes) : null;
  }

  protected void registeHandler(RpcProxyObject rpcProxyObject) {
    String path = KoalaWatcher.ROOT_PATH + "/rpc/" + rpcProxyObject.implClass.getName();
    watcher.registeHandler(path, rpcProxyObject);
  }
}
