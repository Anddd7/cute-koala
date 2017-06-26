package github.koala.webservice.resetful.annotation;

import github.koala.webservice.resetful.utils.impl.JsonRequestParser;
import github.koala.webservice.resetful.utils.impl.JsonResponseParser;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author edliao on 2017/6/21.
 * @description RPC的远程服务
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HttpKoala {

  String value();

  Class responseParser() default JsonResponseParser.class;

  Class requestParser() default JsonRequestParser.class;
}
