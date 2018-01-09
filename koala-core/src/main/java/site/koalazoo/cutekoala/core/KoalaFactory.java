package site.koalazoo.cutekoala.core;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import site.koalazoo.cutekoala.bean.KoalaManager;

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

  /**
   * koala扫描器
   */
  private KoalaScanner scanner = new KoalaScanner();
  /**
   * koala缓存
   */
  private KoalaManager pool = new KoalaManager();


  public void init(Class target) {
    List<Class> koalaClasses = scanner.scanClasspath(target);
    log.info("搜索到{}个koala", koalaClasses.size());
    koalaClasses.forEach(pool::addKoala);
  }


}
