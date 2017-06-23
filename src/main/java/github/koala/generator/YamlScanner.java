package github.koala.generator;

import github.koala.generator.domain.ConfigDefine;
import java.io.FileReader;
import java.net.URL;
import org.yaml.snakeyaml.Yaml;

/**
 * @author edliao on 2017/6/23.
 * @description 解析yaml文件
 */
public class YamlScanner {

  private static final URL CONFIG_FILE_URL;
  private static final Yaml scanner;

  static {
    CONFIG_FILE_URL = YamlScanner.class.getClassLoader()
        .getResource("template/modules-define.yaml");
    scanner = new Yaml();
  }

  public ConfigDefine getConfig() {
    ConfigDefine config = null;
    try {
      config = scanner.loadAs(new FileReader(CONFIG_FILE_URL.getFile()), ConfigDefine.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return config;
  }
}
