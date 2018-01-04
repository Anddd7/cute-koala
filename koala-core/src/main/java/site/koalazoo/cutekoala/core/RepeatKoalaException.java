package site.koalazoo.cutekoala.core;

/**
 * @author and777
 * @date 2018/1/3
 */
public class RepeatKoalaException extends RuntimeException {

  public RepeatKoalaException(String name, Class existClass, Class newClass) {
    super(String.format("Koala名称冲突:%s - %s | %s", name, existClass.toGenericString(),
        newClass.toGenericString()));
  }
}
