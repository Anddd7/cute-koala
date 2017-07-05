package github.koala.zookeeper;

import com.google.common.base.Joiner;
import github.and777.common.FormatTool;
import github.koala.zookeeper.config.KoalaTreeConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author edliao on 2017/6/27.
 * @description Client连接
 */
@Slf4j
public class TreeWatcherClient implements Watcher {

  private static final Integer TIMEOUT = 5000;

  private ZooKeeper server;
  private KoalaTreeConfig config;

  private AbstractEventHandler handler;

  /**
   * 连接ZooKeeper服务器
   */
  public TreeWatcherClient(KoalaTreeConfig config) {
    this.config = config;
    try {
      this.server = new ZooKeeper(config.getConnectString(), TIMEOUT, this);
      synchronized (this) {
        this.wait();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 事件处理
   */
  @Override
  public void process(WatchedEvent watchedEvent) {
    log.info("接受到事件:状态[{}],路径[{}]", watchedEvent.getState().toString(), watchedEvent.getPath());
    String path = watchedEvent.getPath();

    if (watchedEvent.getState() == KeeperState.SyncConnected) {
      switch (watchedEvent.getType()) {
        case None:
          synchronized (this) {
            this.notifyAll();
          }
          break;

        case NodeDeleted:
          log.info("删除节点{}", path);
          break;

        case NodeCreated:
          log.info("创建节点{}", path);
          break;

        case NodeDataChanged:
          Optional<byte[]> data = listenData(path);
          data.ifPresent(d -> handler.handleChange(path, d));
          log.info("节点{}数据变更:{}", path, data);
          break;

        case NodeChildrenChanged:
          log.info("节点{}下子节点发生变化", path);
          break;
      }
    }
  }


  /**
   * 公共方法
   */
  private Optional<Stat> listenNode(String nodePath) {
    Stat stat = null;
    try {
      stat = server.exists(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(stat);
  }

  private Boolean existsNode(String nodePath) {
    Boolean exists = listenNode(nodePath).isPresent();
    if (exists) {
      log.info("获取到节点{}\n{}", nodePath, formatStatInfo(listenNode(nodePath).get()));
    } else {
      log.info("节点{}不存在", nodePath);
    }
    return exists;
  }

  /**
   * 获取groupNode下的数据 ,并监听其变化
   */
  private Optional<byte[]> listenData(String nodePath) {
    byte[] bytes = null;
    try {
      bytes = server.getData(nodePath, true, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(bytes);
  }

  /**
   * 获取groupNode下的子节点 ,并监听子节点的变化
   */
  private List<String> listenChildren(String nodePath) {
    List<String> children = new ArrayList<>();
    try {
      children = server.getChildren(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!children.isEmpty()) {
      log.info("获取到{}的子节点:{}", nodePath, String.join(",", children));
    }
    return children;
  }

  /**
   * 监听节点/数据和子节点的节点/数据变化
   */
  private void listenPath(String nodePath) {
    listenNode(nodePath);
    listenData(nodePath);
    listenChildren(nodePath).forEach(childName -> listenPath(nodePath + "/" + childName));
  }

  /**
   * 对外接口 ,存取数据节点
   */
  public Optional<byte[]> getData(String path) {
    return listenData(path);
  }

  public void setData(String path, byte[] data) {
    setData(path, data, CreateMode.EPHEMERAL);
  }

  public void setData(String path, byte[] data, CreateMode createMode) {
    try {
      Optional<Stat> stat = listenNode(path);
      if (stat.isPresent()) {
        server.setData(path, data, stat.get().getVersion());
      } else {
        server.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test
   */
  public String formatStatInfo(Stat stat) {
    Map<String, String> map = FormatTool.formatArray2Map(
        new String[]{"czxid", "mzxid", "ctime", "mtime", "version", "cversion", "aversion",
            "ephemeralOwner", "dataLength", "numChildren", "pzxid"},
        stat.toString().trim().split(","));

    return "\t" + Joiner.on(",\n\t").withKeyValueSeparator("=").join(map);
  }
}
