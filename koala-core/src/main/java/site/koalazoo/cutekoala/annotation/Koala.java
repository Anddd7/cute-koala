package site.koalazoo.cutekoala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import site.koalazoo.cutekoala.common.KoalaType;

/**
 * @author and777
 * @date 2018/1/3
 *
 * 标识需要扫描生成bean的类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Koala {

  String align() default "";

  KoalaType type() default KoalaType.Singleton;

  /**
   * 发布成远程服务
   */
  boolean isProvider() default false;

}
