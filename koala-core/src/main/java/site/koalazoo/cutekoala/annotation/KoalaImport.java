package site.koalazoo.cutekoala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author and777
 * @date 2018/1/5
 *
 * 表示需要注入的属性
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KoalaImport {

  /**
   * 是否依赖远程的koala
   */
  boolean isConsumer() default false;

  /**
   * 优先使用远程的bean
   */
  boolean useRemoteFirst() default false;

}
