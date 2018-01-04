package github.koala;

import com.alibaba.fastjson.JSON;
import com.koala.mvc.KoalaServletTest;
import com.koala.normal.ExportModule;
import com.koala.normal.ImportModule;
import com.koala.normal.Module;
import com.koala.normal.services.Service1;
import com.koala.normal.services.Service2;
import com.koala.normal.services.Service3;
import com.koala.orm.dao.ActorDao;
import com.koala.orm.dao.impl.ActorDaoImpl;
import com.koala.orm.domian.Actor;
import com.koala.rpc.OrderModule;
import com.koala.rpc.UserModule;
import com.koala.rpc.beans.User;
import com.koala.rpc.services.OrderService;
import com.koala.rpc.services.UserService;
import github.koala.core.KoalaTree;
import github.koala.mvc.KoalaOnJetty;
import github.koala.orm.DataBasePool;
import github.koala.orm.conn.DBConnection;
import github.koala.orm.conn.MysqlConnection;
import github.koala.orm.util.Generator;
import java.util.List;
import org.junit.Test;

public class FactoryTest {

  @Test
  public void scanModule() {
    KoalaTree factory = KoalaTree.of(Module.class);
    //    Service1 service1 = factory.getKoala(Service1Impl.class);
    //    Service1 service11 = factory.getKoala(Service1.class);
    //
    //    service1.say();
    //    service11.say();

    Service2 service2 = factory.getKoala(Service2.class);
    service2.say3();
    service2.say();

    Service3 service3 = factory.getKoala(Service3.class);
    service3.say();
  }

  @Test
  public void rpcTest() throws InterruptedException {
    KoalaTree exportFactory = KoalaTree.of(65001, ExportModule.class);
    KoalaTree importFactory = KoalaTree.of(65002, ImportModule.class);

    Thread.sleep(1000 * 5);

    Thread t1 = new Thread(() -> {
      Service1 s11 = exportFactory.getKoala(Service1.class);
      s11.say();
    }, "Export->>>");

    Thread t2 = new Thread(() -> {
      Service1 s12 = importFactory.getKoala(Service1.class);
      s12.say();
    }, "Import<<<-");

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }

  @Test
  public void rpcTest2() throws InterruptedException {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    DataBasePool.addDBConnection(connection);

    KoalaTree user = KoalaTree.of(65001, UserModule.class);
    System.out.println("\n\n\n");
    KoalaTree order = KoalaTree.of(65002, OrderModule.class);

    Thread.sleep(1000 * 5);

    Thread t1 = new Thread(() -> {
      //UserService userService = user.getKoala(UserService.class);
    }, "Export->>>");

    Thread t2 = new Thread(() -> {
      OrderService orderService = order.getKoala(OrderService.class);
      UserService userService = order.getKoala(UserService.class);

      User eddy = userService.getUser("eddy", 23);
      orderService.createOrder(eddy);
    }, "Import<<<-");

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }

  @Test
  public void testORMGenerate() {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    Generator generator = new Generator(connection);
    generator.generate("src/scanModule/java", "com.koala.orm", "actor");
  }

  @Test
  public void testORMCall() {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    DataBasePool.addDBConnection(connection);
    ActorDao dao = new ActorDaoImpl();
    Actor example = new Actor();
    //example.setActorId(195);
    List<Actor> actors = dao.selectByExample(example, 0, 10);
    actors.forEach(actor -> System.out.println(JSON.toJSONString(actor)));
  }

  @Test
  public void testJetty() throws Exception {
    DBConnection connection = new MysqlConnection("localhost:3306", "sakila", "root", "root");
    DataBasePool.addDBConnection(connection);

    KoalaOnJetty.startKoalaOnJetty(9999, UserModule.class, KoalaServletTest.class);
  }
}
