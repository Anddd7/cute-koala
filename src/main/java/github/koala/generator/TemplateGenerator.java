package github.koala.generator;

import com.google.common.base.CaseFormat;
import com.google.common.io.Files;
import github.koala.generator.domain.ConfigDefine;
import github.koala.generator.domain.ModuleDefine;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
 * @author edliao on 2017/6/23.
 * @description 模块模板生成器
 */
@Slf4j
public class TemplateGenerator {

  private static final String TEMP_ROOT_PATH = "template/";
  private static final String DEFAULT_TEMPLATE = "/koala-module.beelt";
  GroupTemplate gt;

  public TemplateGenerator() {
    try {
      ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(TEMP_ROOT_PATH);
      Configuration cfg = Configuration.defaultConfiguration();
      gt = new GroupTemplate(resourceLoader, cfg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void generate(String sourcePath, ConfigDefine configDefine) {
    for (ModuleDefine moduleDefine : configDefine.getModules()) {
      Template t = gt.getTemplate(DEFAULT_TEMPLATE);
      t.binding("module", moduleDefine);
      String filePath = new StringBuilder()
          .append(sourcePath)
          .append(moduleDefine.getPackageName().replace(".", "/"))
          .append("/")
          .append(moduleDefine.getModuleName())
          .append(".java")
          .toString();

      log.info("生成文件路径{}", filePath);
      write2JavaFile(filePath, t.render());
    }
  }

  public void write2JavaFile(String filePath, String content) {
    try {
      File file = new File(filePath);
      if (!file.exists()) {
        Files.createParentDirs(file);
        file.createNewFile();
      }
      Files.write(content.getBytes(), file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
