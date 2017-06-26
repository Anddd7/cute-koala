package github.koala;

import com.koala.services.UserService;
import github.koala.common.FileSystemTool;
import github.koala.core.factory.KoalaFactory;
import github.koala.generator.TemplateGenerator;
import github.koala.generator.YamlScanner;
import org.junit.Test;

/**
 * @author edliao on 2017/6/26.
 * @description 模板生成
 */
public class TemplateGeneratorTest {

  @Test
  public void testYaml() {
    new TemplateGenerator()
        .generate(FileSystemTool.getProjectPath() + "/generator-test/",
            new YamlScanner().getConfig());
  }

  @Test
  public void testWithBeanFactory()  {
    new TemplateGenerator()
        .generate(FileSystemTool.getProjectPath() + "/src/test/java/",
            new YamlScanner().getConfig());
  }

  @Test
  public void test() throws ClassNotFoundException {
    KoalaFactory beanFactory = KoalaFactory.of(Class.forName("com.koala.UserHttpModule"));
    beanFactory.getBean(UserService.class).welcome();
  }
}
