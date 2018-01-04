package site.koalazoo.cutekoala.rpc;

/**
 * @author and777
 * @date 2018/1/4
 */
public class RemoteKoalaFactory {

  private static final RemoteKoalaFactory FACTORY = new RemoteKoalaFactory();

  public static Object getRemoteHandler(Class clazz) {
    return FACTORY.getRemoteKoala(clazz);
  }

  private Object getRemoteKoala(Class clazz) {
    return null;
  }
}
