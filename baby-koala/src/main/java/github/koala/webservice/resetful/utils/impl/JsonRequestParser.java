package github.koala.webservice.resetful.utils.impl;

import com.alibaba.fastjson.JSON;
import github.and777.common.CollectionTool;
import github.koala.webservice.resetful.annotation.HttpKoalaParameter;
import github.koala.webservice.resetful.utils.AbstractRequestParser;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author edliao on 2017/6/22.
 * @description 使用JSON序列化Request请求
 */
@Slf4j
public class JsonRequestParser implements AbstractRequestParser {

  private static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

  @Override
  public MediaType getMediaType() {
    return mediaType;
  }

  @Override
  public String formatParameter2Url(Method method, Object[] args) {
    if (CollectionTool.isEmpty(args)) {
      return "";
    }

    List<String> paramExpr = new ArrayList<>();
    checkMethodAndParameter(method, args, (s, s2) -> paramExpr.add(s + "=" + s2));

    return "?" + String.join("&", paramExpr);
  }

  @Override
  public String formatParameter2Body(Method method, Object[] args) {
    if (CollectionTool.isEmpty(args)) {
      return "";
    }

    if (args.length == 1) {
      return args[0] instanceof String ? args[0].toString() : JSON.toJSONString(args[0]);
    }

    Map<String, Object> paramExpr = new HashMap<>();
    checkMethodAndParameter(method, args, paramExpr::put);

    return JSON.toJSONString(paramExpr);
  }

  private void checkMethodAndParameter(Method method, Object[] args,
      BiConsumer<String, String> biConsumer) {
    for (int i = 0; i < args.length; i++) {
      String paramName = method.getParameters()[i].getAnnotation(HttpKoalaParameter.class).value();
      String param = args[i] instanceof String ? args[i].toString() : JSON.toJSONString(args[i]);
      biConsumer.accept(paramName, param);
    }
  }

  @Override
  public void checkRequest(Request request) {
    log.info("发起请求:{}", request.toString());

    RequestBody body = request.body();
    if (request.body() != null) {
      log.info("请求报文体:[{}]-{}", body.contentType(), request.body());
    }
  }
}
