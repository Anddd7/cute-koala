package github.koala.rpc.zookeeper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import github.eddy.common.FormatTool;
import github.koala.rpc.zookeeper.config.KoalaForestConfig;
import java.util.ArrayList;
import java.util.HashMap;
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
public class KoalaWatcher implements Watcher {

  private static final Integer TIMEOUT = 5000;
  public static final String ROOT_PATH = "/koala";

  private ZooKeeper zookeeper;
  private Map<String, ServiceEventHandler> handlerMap = new HashMap<>();

  /**
   * 连接ZooKeeper服务器
   */
  public KoalaWatcher() {
    String connectString = KoalaForestConfig.getRpcConfig().getConnectString();
    try {
      this.zookeeper = new ZooKeeper(connectString, TIMEOUT, this);
      synchronized (this) {
        this.wait();
      }
    } catch (Exception e) {
      log.error("Can't connect with zookeeper server:{}", connectString);
      //System.exit(0);
    }
  }

  public void registeHandler(String path, ServiceEventHandler handler) {
    handlerMap.putIfAbsent(path, handler);
  }

  /**
   * 事件处理
   */
  @Override
  public void process(WatchedEvent watchedEvent) {
    log.info("接受到事件:状态[{}],路径[{}]", watchedEvent.getState().toString(), watchedEvent.getPath());
    String path = watchedEvent.getPath();

    if (watchedEvent.getState() == KeeperState.SyncConnected) {
      ServiceEventHandler handler = handlerMap.get(path);
      switch (watchedEvent.getType()) {
        case None:
          synchronized (this) {
            this.notifyAll();
          }
          break;

        case NodeDeleted:
          log.info("删除节点{}", path);
          handler.handleServiceDelete();
          break;

        case NodeCreated:
          log.info("创建节点{}", path);
          // handler.handleServiceAdd(path, listenData(path));
          break;

        case NodeDataChanged:
          // handler.handleServiceChange(path, listenData(path));
          log.info("节点{}数据变更", path);
          break;

        case NodeChildrenChanged:
          log.info("节点{}下子节点发生变化:[{}]", path, String.join(",", listenChildren(path)));
          break;
      }
    }
  }


  /**
   * 监听Node信息
   */
  private Optional<Stat> listenNode(String nodePath) {
    Stat stat = null;
    try {
      stat = zookeeper.exists(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(stat);
  }

  /**
   * 检查Node是否存在 ,不监听
   */
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
   * 创建多级目录
   */
  public void mkdirs(String path) throws Exception {
    List<String> dirs = Splitter.on("/").omitEmptyStrings().splitToList(path);

    String currentPath = "";
    for (String dir : dirs) {
      currentPath = currentPath + "/" + dir;
      log.info("检查{}路径是否创建", currentPath);

      if (zookeeper.exists(currentPath, false) == null) {
        zookeeper.create(currentPath, dir.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.PERSISTENT);
      }
    }
  }

  /**
   * 获取Node数据 ,并监听
   */
  private byte[] listenData(String nodePath) {
    byte[] bytes = null;
    try {
      bytes = zookeeper.getData(nodePath, true, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (bytes != null) {
      log.info("获取节点数据{}-{}", nodePath, new String(bytes));
    }
    return bytes;
  }

  /**
   * 获取Node下的子节点 ,并监听子节点
   */
  private List<String> listenChildren(String nodePath) {
    List<String> children = new ArrayList<>();
    try {
      children = zookeeper.getChildren(nodePath, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (!children.isEmpty()) {
      log.info("获取到{}的子节点:{}", nodePath, String.join(",", children));
    }
    return children;
  }

  /**
   * 递归检测Node及Node的子节点
   */
  private void listenPath(String nodePath) {
    listenNode(nodePath);
    listenData(nodePath);
    listenChildren(nodePath).forEach(childName -> listenPath(nodePath + "/" + childName));
  }

  /**
   * 对外接口 ,存取数据节点
   */
  public byte[] getData(String path) {
    return listenData(path);
  }

  public void setData(String path, byte[] data) {
    setData(path, data, CreateMode.EPHEMERAL);
  }

  public void setData(String path, byte[] data, CreateMode createMode) {
    try {
      Optional<Stat> stat = listenNode(path);
      if (stat.isPresent()) {
        zookeeper.setData(path, data, stat.get().getVersion());
      } else {
        zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
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
