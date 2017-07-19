package github.koala.rpc.message;

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
    //Object jsonObject = JSON.toJSONString();
    if (resultObject instanceof JSONArray) {
      return ((JSONArray) resultObject).toJavaObject(resultType);
    } else if (resultObject instanceof JSONObject) {
      return ((JSONObject) resultObject).toJavaObject(resultType);
    } else {
      return resultObject;
    }
  }
}
