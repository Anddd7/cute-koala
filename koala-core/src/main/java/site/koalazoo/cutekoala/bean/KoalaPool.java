package site.koalazoo.cutekoala.bean;

import site.koalazoo.cutekoala.core.RepeatKoalaException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author and777
 * @date 2018/1/3
 *
 * Bean管理中心
 */
public class KoalaPool {
  Map<String,Koala> name2Koala = new HashMap<>();
  Map<Class,String> class2name = new HashMap<>();

  public void addKoala(String name,Class clazz){
    if (name2Koala.containsKey(name)){
      throw new RepeatKoalaException(name,name2Koala.getClass(),clazz);
    }
    if (name2Koala.containsKey(name)){
      throw new RepeatKoalaException(name,name2Koala.getClass(),clazz);
    }


  }


}
