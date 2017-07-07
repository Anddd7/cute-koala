package github.koala.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/7/7.
 * @description TODO
 */

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcResponseProtocol {

  Class resultType;
  Object resultObject;

  public String serialization() {
    return JSON.toJSONString(this);
  }

  public static RpcResponseProtocol deserialization(String desc) {
    return JSON.parseObject(desc, RpcResponseProtocol.class);
  }

  public Object deserializationResultObject(Class resultType) {
    Object jsonObject = JSON.toJSONString(resultObject);
    if (jsonObject instanceof JSONArray) {
      return ((JSONArray) jsonObject).toJavaObject(resultType);
    } else if (jsonObject instanceof JSONObject) {
      return ((JSONObject) jsonObject).toJavaObject(resultType);
    } else {
      return jsonObject;
    }
  }
}
