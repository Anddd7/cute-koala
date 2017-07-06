package github.koala.zookeeper;

/**
 * @author edliao on 2017/7/5.
 * @description 处理ZooKeeper 服务中心 指定路径下的相应事件
 */
public abstract class AbstractEventHandler {

  public abstract void handleServiceChange(String path, byte[] data);

  public  abstract void handleServiceDelete(String path);

  public  abstract void handleServiceAdd(String path, byte[] data);
}
