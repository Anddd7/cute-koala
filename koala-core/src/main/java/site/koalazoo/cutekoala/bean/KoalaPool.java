package site.koalazoo.cutekoala.bean;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean管理中心
 */
public class KoalaPool {

  Map<String, Koala> classname2Koala = new HashMap<>();

  public void containsKoala(String classname) {
    Preconditions.checkArgument(!classname2Koala.containsKey(classname), "已存在Koala-%s", classname);
  }

  public void addKoala(String classname, Koala koala) {
    classname2Koala.put(classname, koala);
  }


}
