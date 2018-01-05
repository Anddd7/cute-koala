package site.koalazoo.cutekoala.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * @author and777
 * @date 2018/1/3
 */
public class KoalaFactoryTests {

  @Test
  public void scanClasses() throws IOException {
   KoalaFactory.run(KoalaFactoryTests.class);
  }


}
