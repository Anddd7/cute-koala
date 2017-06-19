package github.koala.core;

import com.koala.UserModule;
import github.koala.core.scan.BeanScanner;
import org.junit.Test;

/**
 * @author edliao on 2017/6/19.
 * @description TODO
 */
public class BeanScannerTest {

  @Test
  public void scanTest() {
    BeanScanner scanner = new BeanScanner();
    scanner.scanModule(UserModule.class);
  }
}
