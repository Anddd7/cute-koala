package github.koala.rpc.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/6.
 * @description 简单的协议封装
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcRequestProtocol {

  String serviceName;
  String methodName;
  List<Object> parameters;

  public String serialization() {
    return JSON.toJSONString(this);
  }

  public List<Object> deserializationParameters(Class[] parameterTypes) {
    List<Object> resultParameters = new ArrayList<>();

    for (int i = 0; i < this.parameters.size(); i++) {
      log.info("转换参数{}到{}类型", i, parameterTypes[i].getName());
      Object jsonObject = JSON.parse(JSON.toJSONString(this.getParameters().get(i)));
      if (jsonObject instanceof JSONArray) {
        resultParameters.add(((JSONArray) jsonObject).toJavaObject(parameterTypes[i]));
      } else if(jsonObject instanceof JSONObject) {
        resultParameters.add(((JSONObject) jsonObject).toJavaObject(parameterTypes[i]));
      }else{
        resultParameters.add(jsonObject);
      }
    }

    return resultParameters;
  }

  public static RpcRequestProtocol deserialization(String desc) {
    return JSON.parseObject(desc, RpcRequestProtocol.class);
  }
}
