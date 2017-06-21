package github.koala.core.rpc;

import com.alibaba.fastjson.JSON;
import github.koala.core.annotation.HttpKoala;
import github.koala.core.annotation.HttpKoalaMethod;
import github.koala.core.annotation.HttpKoalaMethod.Request;
import github.koala.core.annotation.HttpKoalaParameter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author edliao on 2017/6/21.
 * @description Http形式的rpc调用
 */
@AllArgsConstructor
@Slf4j
public class HttpHandler implements InvocationHandler {

  String className;
  String rootUrl;
  AbstractResponseParser parser;
  HttpClient httpClient;

  public static Object getProxyObject(Class<?> classType) {
    String url = classType.getAnnotation(HttpKoala.class).value();
    Class responseParser = classType.getAnnotation(HttpKoala.class).responseParser();
    HttpClient httpClient = new HttpClient();

    if (responseParser.isAssignableFrom(AbstractResponseParser.class)) {
      log.error("对应的消息解析器必须继承自AbstractResponseParser ,获取Bean失败.");
      return null;
    }

    AbstractResponseParser parser = null;
    try {
      parser = (AbstractResponseParser) responseParser.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    log.info("生成[{}]远程代理Bean,使用[{}]进行结果解析", classType.getName(), responseParser.getName());
    return Proxy.newProxyInstance(classType.getClassLoader(), new Class[]{classType},
        new HttpHandler(classType.getName(), url, parser, httpClient));
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object parentMethodResult = checkProxyMethod(method, args);
    if (parentMethodResult != null) {
      return parentMethodResult;
    }

    log.info("[{}]调用远程方法[{}].", className, method.getName());
    String url = rootUrl + method.getAnnotation(HttpKoalaMethod.class).value();

    Request requestType = method.getAnnotation(HttpKoalaMethod.class).type();
    String response;
    if (requestType.equals(Request.GET)) {
      response = httpClient.get(url + joinParameters(method, args));
    } else {
      response = httpClient.post(url, formatParameters(method, args));
    }
    return parser.parserResponseString(response, method.getReturnType());
  }

  private Object checkProxyMethod(Method method, Object[] args) {
    if (method.getName().equals("toString")) {
      return super.toString();
    } else if (method.getName().equals("equals")) {
      return super.equals(args[0]);
    }
    return null;
  }


  public String joinParameters(Method method, Object[] args) {
    if (args == null || args.length == 0) {
      return "";
    }
    List<String> paramExpr = new ArrayList<>();
    for (int i = 0; i < args.length; i++) {
      String paramName = method.getParameters()[i].getAnnotation(HttpKoalaParameter.class).value();
      String param = args[i] instanceof String ? args[i].toString() : JSON.toJSONString(args[i]);
      paramExpr.add(paramName + "=" + param);
    }
    return "?" + String.join("&", paramExpr);
  }

  private String formatParameters(Method method, Object[] args) {
    if (args == null || args.length == 0) {
      return null;
    }

    if (args.length == 1) {
      return args[0] instanceof String ? args[0].toString() : JSON.toJSONString(args[0]);
    }

    Map<String, Object> paramExpr = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      String paramName = method.getParameters()[i].getAnnotation(HttpKoalaParameter.class).value();
      String param = args[i] instanceof String ? args[i].toString() : JSON.toJSONString(args[i]);
      paramExpr.put(paramName, param);
    }
    return JSON.toJSONString(paramExpr);
  }
}
