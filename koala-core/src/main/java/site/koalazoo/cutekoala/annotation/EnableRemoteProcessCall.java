package site.koalazoo.cutekoala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author and777
 * @date 2018/1/10
 *
 * 开启远程调用注册
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableRemoteProcessCall {

  /**
   * Zookeeper注册中心地址
   */
  String[] url();
}
