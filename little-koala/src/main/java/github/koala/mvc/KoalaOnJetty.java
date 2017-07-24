package github.koala.mvc;

import github.koala.core.KoalaTree;
import javax.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class KoalaOnJetty {

  static KoalaTree tree;

  public static <T extends Servlet> void startKoalaOnJetty(int port, Class module, Class<T> servlet)
      throws Exception {
    Server server = new Server(port);

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    context.addServlet(servlet, "/");

    HandlerCollection handlers = new HandlerCollection(context, new DefaultHandler());
    server.setHandler(handlers);

    tree = KoalaTree.of(module);

    server.start();
    server.join();
  }
}
