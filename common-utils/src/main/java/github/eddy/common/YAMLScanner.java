package github.eddy.common;

import java.io.FileReader;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

/**
 * @author edliao on 2017/6/23.
 * @description 解析yaml文件
 */
@Slf4j
public class YAMLScanner {

  private YAMLScanner() {
  }

  private static final Yaml scanner = new Yaml();

  /**
   * 按class类型加载yaml配置
   */
  private static <T> T loadConfig(URL url, Class<T> targetClass) {
    T config = null;
    try {
      config = scanner.loadAs(new FileReader(url.getFile()), targetClass);
    } catch (Exception e) {
      log.error("", e);
    }
    return config;
  }

  public static <T> T getConfig(URL url, Class<T> targetClass) {
    return loadConfig(url, targetClass);
  }

  public static <T> T getConfigInClassPath(String path, Class<T> targetClass) {
    return getConfig(YAMLScanner.class.getClassLoader().getResource(path), targetClass);
  }

}
