package github.koala.core.utils;

import okhttp3.Response;

/**
 * @author edliao on 2017/6/21.
 * @description ResponseParser 的基类
 */
public abstract class AbstractResponseParser {

  /**
   * 解析返回报文
   */
  public abstract <T> T parserResponse(Response response, Class<T> resultType);
}
