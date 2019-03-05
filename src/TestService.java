import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/HelloWorld")
public class TestService {
    @GET
    @Path("/sayHello")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello() {
        return "<h1>Hello World</h1>";
    }

}
