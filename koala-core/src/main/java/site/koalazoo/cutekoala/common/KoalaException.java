package site.koalazoo.cutekoala.common;

/**
 * @author and777
 * @date 2018/1/3
 */
public class KoalaException extends RuntimeException {

  public KoalaException(String message) {
    this(message, null);
  }

  public KoalaException(Throwable cause) {
    this("", cause);
  }

  public KoalaException(String message, Throwable cause) {
    super(message, cause);
  }


  public static KoalaException notExist(String path, Throwable cause) {
    return new KoalaException(String.format("找不到对应的类或资源:%s", path), cause);
  }

  public static KoalaException invalidClasspath(Throwable cause) {
    return new KoalaException("入口类无效或classpath有误", cause);
  }

  public static KoalaException createError(String align, KoalaType type, Class newClass) {
    return new KoalaException(
        String.format("Koala创建失败:%s - %s | %s",
            align,
            type,
            newClass.toGenericString())
    );
  }
}
