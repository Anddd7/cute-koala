package site.koalazoo.cutekoala.bean;

import lombok.AllArgsConstructor;
import site.koalazoo.cutekoala.core.BeanType;
import site.koalazoo.cutekoala.rpc.RemoteKoalaFactory;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean包装器
 */
@AllArgsConstructor
public class Koala {

  String align;
  BeanType type;
  Class clazz;
  Object object;

  public static Koala createKoala(BeanType type, Class clazz) {
    return createKoala(type, clazz);
  }

  public static Koala createKoala(String align, BeanType type, Class clazz)
      throws IllegalAccessException, InstantiationException {
    switch (type) {
      case Singleton:
      case Consumer:
        return new Koala(align, type, clazz, clazz.newInstance());
      case Multiple:
        return new Koala(align, type, clazz, null);
      case Provider:
        return new Koala(align, type, clazz, RemoteKoalaFactory.getRemoteHandler(clazz));
      default:
        return null;
    }
  }


}
