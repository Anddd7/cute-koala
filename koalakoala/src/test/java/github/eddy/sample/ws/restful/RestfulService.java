package github.eddy.sample.ws.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("rest")
public class RestfulService {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String hello() {
    return "hello";
  }
}