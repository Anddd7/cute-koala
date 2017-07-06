package github.koala.rpc;

import github.koala.rpc.consumer.RpcProxyObject;
import github.koala.zookeeper.AbstractEventHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public class RpcEventHandler extends AbstractEventHandler {

  Map<String, RpcProxyObject> path2ProxyObj = new HashMap<>();

  @Override
  public void handleServiceChange(String path, byte[] data) {
    Optional.ofNullable(path2ProxyObj.get(path2ProxyObj))
        .ifPresent(rpcProxyObject -> rpcProxyObject.setRpcUrl(new String(data)));
  }

  @Override
  public void handleServiceDelete(String path) {

  }

  @Override
  public void handleServiceAdd(String path, byte[] data) {

  }
}
