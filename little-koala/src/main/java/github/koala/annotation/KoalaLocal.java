package github.koala.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * beans which stores in local Koala Zoo
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface KoalaLocal {

  //store implement clazz type
  Class<?> value() default KoalaLocal.class;

  ScopeEnum scope() default ScopeEnum.SINGLETON;


  /**
   * Singleton or noscope ( new instance every time )
   */
  enum ScopeEnum {
    SINGLETON("Singleton"), NOSCOPE("NoScope");

    String name;

    ScopeEnum(String name) {
      this.name = name;
    }

    public String toString() {
      return name;
    }
  }
}
