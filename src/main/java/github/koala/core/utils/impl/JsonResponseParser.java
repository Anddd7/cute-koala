package github.koala.core.utils.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import github.koala.core.utils.AbstractResponseParser;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

/**
 * @author edliao on 2017/6/21.
 * @description 使用JSON解析返回的response
 */
@Slf4j
public class JsonResponseParser extends AbstractResponseParser {

  @Override
  public <T> T parserResponse(Response response, Class<T> resultType) {
    String body = getBody(response);

    log.info("收到回复:{}", response.toString());
    log.info("回复报文体:[{}]-{}", response.body().contentType().toString(), body);
    log.info("返回对象[{}]", resultType.getName());

    Object jsonObject = JSON.parse(body);
    if (jsonObject instanceof JSONArray) {
      return ((JSONArray) jsonObject).toJavaObject(resultType);
    }
    return ((JSONObject) jsonObject).toJavaObject(resultType);
  }

  /**
   * 获取报文体
   */
  String getBody(Response response) {
    try {
      return response.body().string();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
