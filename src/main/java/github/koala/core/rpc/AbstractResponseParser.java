package github.koala.core.rpc;

/**
 * @author edliao on 2017/6/21.
 * @description 解析response的接口类
 */
public abstract class AbstractResponseParser {

  public abstract <T> T parserResponseString(String responseString, Class<T> resultType);
}
