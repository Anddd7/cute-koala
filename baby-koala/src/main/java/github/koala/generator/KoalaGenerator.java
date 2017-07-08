package github.koala.generator;

import github.eddy.common.TemplateGenerator;
import github.koala.generator.domain.ConfigDefine;
import github.koala.generator.domain.ModuleDefine;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/23.
 * @description 模块模板生成器
 */
@Slf4j
public class KoalaGenerator {

  private static final String DEFAULT_TEMPLATE = "template/koala-module.beelt";

  TemplateGenerator templateGenerator = new TemplateGenerator();

  /**
   * 把ConfigDefine的内容生成成java文件,放到sourcePath位置
   */
  public void generate(String sourcePath, ConfigDefine configDefine) {
    for (ModuleDefine moduleDefine : configDefine.getModules()) {

      String targetPath = sourcePath + moduleDefine.getPackageName().replace(".", "/");
      String fileName = moduleDefine.getModuleName() + ".java";

      log.info("生成文件{}路径{}", fileName, targetPath);

      templateGenerator.generateTemplateTo(moduleDefine, DEFAULT_TEMPLATE, targetPath, fileName);
    }
  }
}
