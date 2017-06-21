package github.koala.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author edliao on 2017/6/20.
 * @description 描述Bean作用域 ,用于扫描
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Scope {

  enum ScopeEnum {
    SINGLETON, NOSCOPE
  }

  ScopeEnum type() default ScopeEnum.SINGLETON;
}