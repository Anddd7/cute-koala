package github.koala.webservice.resetful.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author edliao on 2017/6/21.
 * @description 远程调用的方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpKoalaMethod {

  String value();

  HttpMethod httpMethod() default HttpMethod.GET;

  enum HttpMethod {
    POST, GET
  }
}