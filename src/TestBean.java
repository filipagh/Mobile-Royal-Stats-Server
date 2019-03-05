import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


//@Stateless(name = "TestEJB")
//@Path("/test")
@ApplicationPath("/rest")
public class TestBean extends Application {

//
//
//    public TestBean() {
//    }
//
//    @GET
//    @Path("/{param}")
//    @Produces("application/json")
//    public Response printMessage(@PathParam("param") String msg) {
//        String result = "Restful example: " + msg;
//        return Response.status(200).entity(result).build();
//    }

}
