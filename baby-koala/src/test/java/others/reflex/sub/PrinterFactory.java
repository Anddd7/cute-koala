package others.reflex.sub;

import net.sf.cglib.proxy.Enhancer;

/**
 * @author edliao on 2017/6/23.
 */
public class PrinterFactory {

  public static Printer getInstance() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(Printer.class);
    //回调方法的参数为代理类对象CglibProxy，最后增强目标类调用的是代理类对象CglibProxy中的intercept方法
    enhancer.setCallback(new ProxyPrinter());

    // 此刻，base不是单纯的目标类，而是增强过的目标类
    return (Printer) enhancer.create();
  }
}
