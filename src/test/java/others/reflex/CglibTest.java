package others.reflex;

import org.junit.Test;
import others.reflex.sub.PrinterFactory;

/**
 * @author edliao on 2017/6/23.
 */
public class CglibTest {

  @Test
  public void testCglib() {
    System.out.println(PrinterFactory.getInstance().print("eddy"));
  }
}
