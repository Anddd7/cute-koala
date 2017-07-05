package github.koala.zookeeper.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;

/**
 * @author edliao on 2017/6/27.
 * @description 节点配置
 */
@Data
public class KoalaTreeConfig {

  /**
   * POJO
   */
  List<String> server;


  /**
   * 分解配置数据
   */
  List<ServerConfig> serverConfigs = new ArrayList<>();

  /**
   * ZooKeeper服务器连接配置
   */
  class ServerConfig {

    String host;
    Integer clientPort;
    Integer leaderPort;
    Integer messagePort;

    ServerConfig(String serverString) {
      String[] parameters = serverString.split(":");
      this.host = parameters[0];
      this.clientPort = Integer.valueOf(parameters[1]);
      this.leaderPort = Integer.valueOf(parameters[2]);
      this.messagePort = Integer.valueOf(parameters[3]);
    }
  }

  void loadConfig() {
    server.forEach(s -> serverConfigs.add(new ServerConfig(s)));
  }

  /**
   * 获取ZooKeeper连接URLs
   */
  public String getConnectString() {
    List<String> urls = serverConfigs.stream()
        .map(serverConfig -> serverConfig.host + ":" + serverConfig.clientPort)
        .collect(Collectors.toList());

    return String.join(",", urls);
  }

}
