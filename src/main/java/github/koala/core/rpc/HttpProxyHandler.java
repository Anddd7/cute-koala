package github.koala.core.rpc;

import github.koala.core.annotation.HttpKoala;
import github.koala.core.annotation.HttpKoalaMethod;
import github.koala.core.annotation.HttpKoalaMethod.HttpMethod;
import github.koala.core.utils.AbstractRequestParser;
import github.koala.core.utils.AbstractResponseParser;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author edliao on 2017/6/21.
 * @description Http形式的rpc调用
 */
@AllArgsConstructor
@Slf4j
public class HttpProxyHandler implements InvocationHandler {

  private String className;
  private String rootUrl;

  private AbstractResponseParser responseParser;
  private AbstractRequestParser requestParser;

  private HttpClientPool httpClient;

  /**
   * 创建代理对象 ,添加序列化工具
   */
  public static Object getProxyObject(Class<?> classType) {
    //检查解析器
    Class<?> resParserClass = classType.getAnnotation(HttpKoala.class).responseParser();
    Class<?> reqParserClass = classType.getAnnotation(HttpKoala.class).requestParser();
    if (resParserClass.isAssignableFrom(AbstractResponseParser.class) && reqParserClass
        .isAssignableFrom(AbstractRequestParser.class)) {
      log.error("对应的消息解析器必须继承自AbstractResponseParser和AbstractRequestParser.");
      return null;
    }

    AbstractResponseParser responseParser = null;
    AbstractRequestParser requestParser = null;
    try {
      responseParser = (AbstractResponseParser) resParserClass.newInstance();
      requestParser = (AbstractRequestParser) reqParserClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    //获取根url
    String url = classType.getAnnotation(HttpKoala.class).value();

    log.info("生成[{}]远程代理Bean,使用[{}]进行结果解析", classType.getName(), resParserClass.getName());

    return Proxy.newProxyInstance(classType.getClassLoader(), new Class[]{classType},
        new HttpProxyHandler(classType.getName(), url, responseParser, requestParser,
            new HttpClientPool()));
  }

  /**
   * 代理方法 ,托管给http完成
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Instant start = Instant.now();

    //屏蔽toString ,equals等方法
    Object parentMethodResult = checkProxyMethod(method, args);
    if (parentMethodResult != null) {
      return parentMethodResult;
    }
    log.info("----------------------------------------------");
    log.info("[{}]调用远程方法[{}].", className, method.getName());

    String url = rootUrl + method.getAnnotation(HttpKoalaMethod.class).value();
    HttpMethod httpMethod = method.getAnnotation(HttpKoalaMethod.class).httpMethod();

    //创建请求request
    Request request = null;
    if (httpMethod.equals(HttpMethod.GET)) {
      request = httpClient.initRequest(url + requestParser.formatParameter2Url(method, args));
    } else {
      request = httpClient.initRequest(url, requestParser.getMediaType(),
          requestParser.formatParameter2Body(method, args));
    }
    //一个切面 可以检查request
    requestParser.checkRequest(request);

    //执行
    Response response = httpClient.execute(request);

    //解析
    Object result = responseParser.parserResponse(response, method.getReturnType());

    log.info("[{}]远程方法[{}]代理执行时间:[{}]", className, method.getName(),
        Duration.between(start, Instant.now()).toMillis());
    log.info("----------------------------------------------\n");
    return result;
  }

  /**
   * 代理接口中属于Object的方法
   */
  private Object checkProxyMethod(Method method, Object[] args) {
    if (method.getName().equals("toString")) {
      return super.toString();
    } else if (method.getName().equals("equals")) {
      return super.equals(args[0]);
    }
    return null;
  }
}
