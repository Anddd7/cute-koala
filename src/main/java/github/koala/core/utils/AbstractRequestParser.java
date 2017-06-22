package github.koala.core.utils;

import java.lang.reflect.Method;
import okhttp3.MediaType;
import okhttp3.Request;

/**
 * @author edliao on 2017/6/22.
 * @description RequestParser 的基类
 */
public abstract class AbstractRequestParser {

  /**
   * 设置ContentType
   */
  public abstract MediaType getMediaType();

  /**
   * 序列化函数参数 ,并添加到url尾部作为url-参数
   */
  public abstract String formatParameter2Url(Method method, Object[] args);

  /**
   * 序列化函数参数 ,作为请求的报文体传输
   */
  public abstract String formatParameter2Body(Method method, Object[] args);

  /**
   * 在参数序列化后 ,可以对request进行更细致的修改和检查
   */
  public abstract void checkRequest(Request request);
}
