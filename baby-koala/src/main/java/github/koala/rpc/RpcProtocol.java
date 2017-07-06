package github.koala.rpc;

import com.alibaba.fastjson.JSON;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description 简单的协议封装
 */
@Slf4j
@AllArgsConstructor
@Data
public class RpcProtocol {

  String serviceName;
  String methodName;
  List<Object> parameters;


  public String serialization() {
    return JSON.toJSONString(this);
  }

  public static RpcProtocol deserialization(String desc) {
    return JSON.parseObject(desc, RpcProtocol.class);
  }

}
