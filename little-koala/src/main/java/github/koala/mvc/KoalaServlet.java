package github.koala.mvc;

import github.koala.core.KoalaTree;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class KoalaServlet extends HttpServlet {

  public KoalaTree tree = KoalaOnJetty.tree;

  @Override
  abstract protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException;

  @Override
  abstract protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException;
}