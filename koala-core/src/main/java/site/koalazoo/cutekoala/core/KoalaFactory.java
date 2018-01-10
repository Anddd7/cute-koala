package site.koalazoo.cutekoala.core;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import site.koalazoo.cutekoala.bean.KoalaManager;
import site.koalazoo.cutekoala.rpc.RemoteKoalaFactory;

/**
 * @author and777
 * @date 2018/1/3
 *
 * 扫描/生成/获取 Koala的工厂
 */
@Slf4j
public class KoalaFactory {

  private static final KoalaFactory FACTORY = new KoalaFactory();

  public static KoalaFactory run(Class target) {
    FACTORY.init(target);
    return FACTORY;
  }

  public static <T> T getKoala(Class<T> clazz) {
    return FACTORY.pool.getKoala(clazz);
  }

  public static Object getKoala(String name) {
    return FACTORY.pool.getKoala(name);
  }
  /*----------------------------------------------------------------------------------------------*/

  /**
   * koala扫描器
   */
  private KoalaScanner scanner = new KoalaScanner();
  /**
   * koala缓存
   */
  private KoalaManager pool = new KoalaManager();

  /**
   * 初始化
   */
  private void init(Class target) {
    RemoteKoalaFactory.run(target);

    List<Class> koalaClasses = scanner.scanClasspath(target);
    log.info("搜索到{}个koala", koalaClasses.size());
    koalaClasses.forEach(pool::addKoala);
  }
}
