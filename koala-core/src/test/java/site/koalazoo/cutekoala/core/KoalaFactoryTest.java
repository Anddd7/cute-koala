package site.koalazoo.cutekoala.core;

import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;
import site.koalazoo.cutekoala.bean.KoalaPool;

/**
 * @author and777
 * @date 2018/1/3
 */
public class KoalaFactoryTest {

  @Test
  public void scanClasses() throws IOException {
    KoalaFactory factory = KoalaFactory.run("");
    Assert.assertEquals(0, 0);
  }

  @Test
  public void addKoala() throws InstantiationException, IllegalAccessException {
    KoalaPool pool = new KoalaPool();
    pool.addKoala("Test", String.class);
  }
}
