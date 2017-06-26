package github.koala.webservice.resetful.utils;

import okhttp3.Response;

/**
 * @author edliao on 2017/6/21.
 * @description ResponseParser 的基类
 */
public interface AbstractResponseParser {

  /**
   * 解析返回报文
   */
 <T> T parserResponse(Response response, Class<T> resultType);
}
