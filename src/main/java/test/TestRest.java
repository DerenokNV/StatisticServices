package test;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/test")
public class TestRest {

  @GET
  public Response addPerson1() {
    return Response
            .status( Response.Status.OK )
            .build();
  }
}
