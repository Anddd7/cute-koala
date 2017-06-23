package github.koala.rpc;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author edliao on 2017/6/23.
 * @description 获取代理对象
 */
public class HttpProxyBeanFactory {

  public static Object getProxyInstance(Class classType) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(classType);
    enhancer.setCallback(new HttpProxyObject(classType));
    return enhancer.create();
  }
}
