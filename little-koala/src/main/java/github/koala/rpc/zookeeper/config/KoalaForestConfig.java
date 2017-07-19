package github.koala.rpc.zookeeper.config;

import github.eddy.common.YAMLScanner;
import java.io.FileNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/27.
 * @description 读取配置文件
 */
@Data
@Slf4j
public class KoalaForestConfig {

  /**
   * POJO
   */
  KoalaTreeConfig rpc;
  KoalaTreeConfig webservice;

  /**
   * 对外的公共方法
   */
  private static final String CONFIG = "koala-forest.yaml";

  private static KoalaForestConfig loadConfig() {
    KoalaForestConfig config = null;
    try {
      config = YAMLScanner.getConfigInClassPath(CONFIG, KoalaForestConfig.class);
      config.getRpc().loadConfig();
      config.getWebservice().loadConfig();
    } catch (FileNotFoundException e) {
      log.error("Don't find config file :{} in classpath", config);
      System.exit(0);
    }
    return config;
  }

  /**
   * static singleton
   */
  private static KoalaForestConfig forestConfig = loadConfig();

  public static KoalaForestConfig getInstance() {
    return forestConfig;
  }

  public static KoalaTreeConfig getRpcConfig() {
    return forestConfig.rpc;
  }

  public static KoalaTreeConfig getWebserviceConfig() {
    return forestConfig.webservice;
  }
}
