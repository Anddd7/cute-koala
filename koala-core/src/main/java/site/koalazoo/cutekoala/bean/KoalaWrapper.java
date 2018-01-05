package site.koalazoo.cutekoala.bean;

import lombok.AllArgsConstructor;
import site.koalazoo.cutekoala.common.KoalaType;
import site.koalazoo.cutekoala.rpc.RemoteKoalaFactory;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean包装器
 */
@AllArgsConstructor
public class KoalaWrapper {

  String align;
  KoalaType type;
  Class clazz;
  Object object;

  public static KoalaWrapper createKoala(KoalaType type, Class clazz) {
    return createKoala(type, clazz);
  }

  public static KoalaWrapper createKoala(String align, KoalaType type, Class clazz)
      throws IllegalAccessException, InstantiationException {
    switch (type) {
      case Singleton:
      case Consumer:
        return new KoalaWrapper(align, type, clazz, clazz.newInstance());
      case Multiple:
        return new KoalaWrapper(align, type, clazz, null);
      case Provider:
        return new KoalaWrapper(align, type, clazz, RemoteKoalaFactory.getRemoteHandler(clazz));
      default:
        return null;
    }
  }


}
