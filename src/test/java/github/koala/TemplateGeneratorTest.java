package github.koala;

import com.koala.services.UserService;
import github.koala.common.FileSystemTool;
import github.koala.common.YAMLScanner;
import github.koala.core.factory.KoalaFactory;
import github.koala.generator.TemplateGenerator;
import github.koala.generator.domain.ConfigDefine;
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
            YAMLScanner.getConfigInClassPath("template/modules-define.yaml", ConfigDefine.class));
  }

  @Test
  public void testWithBeanFactory() {
    new TemplateGenerator()
        .generate(FileSystemTool.getProjectPath() + "/src/test/java/",
            YAMLScanner.getConfigInClassPath("template/modules-define.yaml", ConfigDefine.class));
  }

  @Test
  public void test() throws ClassNotFoundException {
    KoalaFactory beanFactory = KoalaFactory.of(Class.forName("com.koala.UserHttpModule"));
    beanFactory.getBean(UserService.class).welcome();
  }
}
