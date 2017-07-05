package others.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

/**
 * @author edliao on 2017/6/23.
 */
public class BeetlTest {

  @Test
  public void test1() throws IOException {
    StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
    Configuration cfg = Configuration.defaultConfiguration();
    GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

    Template t = gt.getTemplate("hello,${name}");
    t.binding("name", "beetl");

    String str = t.render();
    System.out.println(str);
  }

  @Test
  public void test2() throws IOException {
    ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/");
    Configuration cfg = Configuration.defaultConfiguration();
    GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

    Template t = gt.getTemplate("/temp.beelt");
    t.binding("name", "beetl");

    String str = t.render();
    System.out.println(str);
  }

  @Test
  public void generateModule() throws IOException {
    ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template/");
    Configuration cfg = Configuration.defaultConfiguration();
    GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);

    Template t = gt.getTemplate("/koala-module.beelt");
    t.binding("package", "com.koala");
    t.binding("moduleName", "UserModule");
    t.binding("annotationList", Arrays.asList(
        "github.koala.core.annotation.Koala",
        "github.koala.core.annotation.Koala.ScopeEnum",
        "github.koala.core.annotation.Module"));

    Map<String, String> bean = new HashMap<>();
    bean.put("annotation", "");
    bean.put("class", "");
    bean.put("name", "");

    t.binding("beans", Arrays.asList(bean));

    String str = t.render();
    System.out.println(str);
  }

  @Test
  public void testReadYaml() throws FileNotFoundException {
    //初始化Yaml解析器
    Yaml yaml = new Yaml();
    File f = new File(
        this.getClass().getClassLoader().getResource("template/modules-define.yaml").getFile());
    Object result = yaml.load(new FileInputStream(f));
    System.out.println(result.getClass());
    System.out.println(result);
  }
}
