package github.koala.zookeeper.config;

import github.and777.common.YAMLScanner;
import lombok.Data;

/**
 * @author edliao on 2017/6/27.
 * @description 读取配置文件
 */
@Data
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
    KoalaForestConfig config = YAMLScanner.getConfigInClassPath(CONFIG, KoalaForestConfig.class);
    config.getRpc().loadConfig();
    config.getWebservice().loadConfig();
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
