package github.koala;

import com.koala.RpcBackModule;
import com.koala.RpcModule;
import com.koala.services.RpcFirst;
import com.koala.services.RpcSecond;
import github.koala.core.factory.KoalaFactory;
import org.junit.Test;

/**
 * @author edliao on 2017/7/6.
 * @description TODO
 */
public class RpcTest {

  @Test
  public void startRpc1() throws InterruptedException {
    KoalaFactory factory = KoalaFactory.of(60001, RpcModule.class);

    Thread.sleep(10 * 1000);

    System.out.println("\n\n``````````````````````````\n\n");
    RpcFirst first = factory.getBean(RpcFirst.class);
    RpcSecond second = factory.getBean(RpcSecond.class);
    System.out.println("\n\n``````````````````````````\n\n");
    System.out.println(first.hello("eddy"));
    System.out.println(second.welcome("eddy"));
    System.out.println("\n\n``````````````````````````");
  }

  @Test
  public void startRpc2() throws InterruptedException {
    KoalaFactory factory = KoalaFactory.of(60001, RpcModule.class);
    KoalaFactory factorySecond = KoalaFactory.of(60002, RpcBackModule.class);

    Thread.sleep(3 * 1000);

    System.out.println("\n\n``````````````````````````\n\n");
    RpcFirst first1 = factory.getBean(RpcFirst.class);
    RpcSecond second1 = factory.getBean(RpcSecond.class);

    System.out.println(first1.hello("eddy"));
    System.out.println(second1.welcome("eddy"));
    System.out.println("\n\n``````````````````````````");

    Thread.sleep(3 * 1000);

    System.out.println("\n\n``````````````````````````\n\n");
    RpcFirst first = factorySecond.getBean(RpcFirst.class);
    RpcSecond second = factorySecond.getBean(RpcSecond.class);

    System.out.println(first.hello("22222"));
    System.out.println(second.welcome("2222222"));
    System.out.println("\n\n``````````````````````````");

    Thread.sleep(5*60 * 1000);

  }

}
