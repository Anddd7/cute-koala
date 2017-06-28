package github.koala.generator;

import github.and777.common.FileSystemTool;
import github.koala.generator.domain.ConfigDefine;
import github.koala.generator.domain.ModuleDefine;
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
  GroupTemplate groupTemplate;

  public TemplateGenerator() {
    try {
      ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(TEMP_ROOT_PATH);
      Configuration cfg = Configuration.defaultConfiguration();
      groupTemplate = new GroupTemplate(resourceLoader, cfg);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 把ConfigDefine的内容生成成java文件,放到sourcePath位置
   */
  public void generate(String sourcePath, ConfigDefine configDefine) {
    for (ModuleDefine moduleDefine : configDefine.getModules()) {
      Template template = groupTemplate.getTemplate(DEFAULT_TEMPLATE);
      template.binding("module", moduleDefine);
      String filePath = new StringBuilder()
          .append(sourcePath)
          .append(moduleDefine.getPackageName().replace(".", "/"))
          .append("/")
          .append(moduleDefine.getModuleName())
          .append(".java")
          .toString();

      log.info("生成文件路径{}", filePath);
      FileSystemTool.writeFile(filePath, template.render());
    }
  }
}
