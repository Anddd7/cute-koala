package site.koalazoo.cutekoala.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import site.koalazoo.cutekoala.annotation.Koala;
import site.koalazoo.cutekoala.rpc.RemoteKoalaFactory;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean包装器
 */
@AllArgsConstructor
@Slf4j
@Getter
@Setter
@Accessors(fluent = true)
public class KoalaWrapper {

  String align;
  KoalaType type;
  Class clazz;
  Object object;

  public static KoalaWrapper createKoala(Koala annotation, Class clazz) {
    return createKoala(annotation.align(), annotation.type(), clazz);
  }

  public static KoalaWrapper createKoala(KoalaType type, Class clazz) {
    return createKoala("", type, clazz);
  }

  public static KoalaWrapper createKoala(String align, KoalaType type, Class clazz) {
    switch (type) {
      case Singleton:
      case Consumer:
        try {
          return new KoalaWrapper(align, type, clazz, clazz.newInstance());
        } catch (Exception e) {
          log.error("创建Koala失败:", e);
          throw KoalaException.createError(align, type, clazz);
        }
      case Multiple:
        return new KoalaWrapper(align, type, clazz, null);
      case Provider:
        return new KoalaWrapper(align, type, clazz, RemoteKoalaFactory.getRemoteHandler(clazz));
      default:
        return null;
    }
  }

  public boolean isMultiple() {
    return type.equals(KoalaType.Multiple);
  }

  public boolean hasAlign() {
    return !align.isEmpty();
  }
}
