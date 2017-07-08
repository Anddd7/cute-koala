package github.eddy.common;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */
@Slf4j
public class TemplateGenerator {
  GroupTemplate groupTemplate;

  public TemplateGenerator() {
    try {
      ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("");
      Configuration cfg = Configuration.defaultConfiguration();
      groupTemplate = new GroupTemplate(resourceLoader, cfg);
    } catch (IOException e) {
    }
  }

  /**
   * 把ConfigDefine的内容生成成java文件,放到sourcePath位置
   */
  public void generateTemplateTo(Object object, String templateFileName, String targetPath,
      String targetFileName) {
    Template template = groupTemplate.getTemplate(templateFileName);
    template.binding("koalaTG", object);
    String filePath = new StringBuilder()
        .append(targetPath)
        .append("/")
        .append(targetFileName)
        .toString();

    log.info("生成文件路径{}", filePath);
    FileSystemTool.writeFile(filePath, template.render());
  }
}
