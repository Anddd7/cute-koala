package github.koala.core.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author edliao on 2017/6/21.
 * @description 解析返回的response
 */
public class JsonResponseParser extends AbstractResponseParser {

  @Override
  public <T> T parserResponseString(String responseString, Class<T> resultType) {
    Object jsonObject = JSON.parse(responseString);
    if (jsonObject instanceof JSONArray) {
      return ((JSONArray) jsonObject).toJavaObject(resultType);
    }
    return ((JSONObject) jsonObject).toJavaObject(resultType);
  }
}
