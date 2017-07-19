package github.koala.rpc.zookeeper;

/**
 * @author edliao on 2017/7/5.
 * @description 处理ZooKeeper 服务中心 指定路径下的相应事件
 */
public interface ServiceEventHandler {

  //void handleServiceChange(String path, byte[] data);

  //void handleServiceAdd(String path, byte[] data);

  void handleServiceDelete();
}
