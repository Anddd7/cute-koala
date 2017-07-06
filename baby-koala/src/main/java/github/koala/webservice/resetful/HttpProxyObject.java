package github.koala.webservice.resetful;

import github.koala.webservice.resetful.annotation.HttpKoala;
import github.koala.webservice.resetful.annotation.HttpKoalaMethod;
import github.koala.webservice.resetful.domain.HttpMethodEnum;
import github.koala.webservice.resetful.utils.AbstractRequestParser;
import github.koala.webservice.resetful.utils.AbstractResponseParser;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author edliao on 2017/6/23.
 * @description Http代理对象
 */
@Slf4j
public class HttpProxyObject implements MethodInterceptor {

  private String className;
  private String rootUrl;

  private AbstractResponseParser responseParser;
  private AbstractRequestParser requestParser;

  private HttpClientPool httpClient = new HttpClientPool();

  /**
   * 修改webservice为配置文件配置
   */
  //private RestfulServiceRegistry serviceConfig;

  /**
   * 创建代理对象 ,添加序列化工具
   */
  HttpProxyObject(Class<?> classType) {
    this.className = classType.getName();
    this.rootUrl = classType.getAnnotation(HttpKoala.class).value();

    //检查解析器
    Class<?> resParserClass = classType.getAnnotation(HttpKoala.class).responseParser();
    Class<?> reqParserClass = classType.getAnnotation(HttpKoala.class).requestParser();
    if (resParserClass.isAssignableFrom(AbstractResponseParser.class) && reqParserClass
        .isAssignableFrom(AbstractRequestParser.class)) {
      log.error("对应的消息解析器必须继承自AbstractResponseParser和AbstractRequestParser.");
      return;
    }

    try {
      responseParser = (AbstractResponseParser) resParserClass.newInstance();
      requestParser = (AbstractRequestParser) reqParserClass.newInstance();
    } catch (Exception e) {
      log.error("", e);
      System.exit(0);
    }

    log.info("生成[{}]远程代理Bean,使用[{}]进行结果解析", classType.getName(), resParserClass.getName());
  }

  /**
   * 代理方法 ,托管给http完成
   *
   * @param o 当前的对象
   * @param method 调用的目标方法
   * @param args 方法参数
   * @param methodProxy cglib派生的子对象
   */
  @Override
  public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy)
      throws Throwable {
    Instant start = Instant.now();

    //屏蔽toString ,equals等方法
    Object parentMethodResult = checkProxyMethod(method, args);
    if (parentMethodResult != null) {
      return parentMethodResult;
    }
    log.info("----------------------------------------------");
    log.info("[{}]调用远程方法[{}].", className, method.getName());

    String url = rootUrl + method.getAnnotation(HttpKoalaMethod.class).value();
    HttpMethodEnum httpMethod = method.getAnnotation(HttpKoalaMethod.class).httpMethod();

    //创建请求request
    Request request;
    if (httpMethod.equals(HttpMethodEnum.GET)) {
      request = httpClient.initGet(url + requestParser.formatParameter2Url(method, args));
    } else {
      request = httpClient.initPost(url, requestParser.getMediaType(),
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
