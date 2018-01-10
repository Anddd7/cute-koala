package site.koalazoo.cutekoala.rpc;

import lombok.Getter;
import site.koalazoo.cutekoala.annotation.EnableRemoteProcessCall;

/**
 * @author and777
 * @date 2018/1/4
 */
public class RemoteKoalaFactory {

  private static final RemoteKoalaFactory FACTORY = new RemoteKoalaFactory();

  public static RemoteKoalaFactory run(Class target) {
    FACTORY.init(target);
    return FACTORY;
  }


  public static void publish(Class clazz) {
  }
  /*----------------------------------------------------------------------------------------------*/

  @Getter
  boolean enable = false;

  private void init(Class target) {
    EnableRemoteProcessCall annotation =
        (EnableRemoteProcessCall) target.getAnnotation(EnableRemoteProcessCall.class);
    if (annotation == null) {
      return;
    }
    this.enable = true;
    /**
     * TODO 连接配置中心 ,开启Netty传输数据
     */
  }
}
