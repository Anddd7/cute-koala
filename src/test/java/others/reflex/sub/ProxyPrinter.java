package others.reflex.sub;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author edliao on 2017/6/23.
 */
public class ProxyPrinter implements MethodInterceptor {

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    long start = System.currentTimeMillis();
    System.out.println("正在执行[" + method.getName() + "]方法.");

    Object result = methodProxy.invokeSuper(o, objects);

    System.out.println("方法执行完毕,耗时:[" + (System.currentTimeMillis() - start) + "]ms");

    return result;
  }
}
