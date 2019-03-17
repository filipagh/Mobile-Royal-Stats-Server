import com.github.phillipkruger.apiee.ApieeService;
import io.swagger.annotations.*;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;


//@Stateless(name = "TestEJB")
//@Path("/test")
@SwaggerDefinition(info = @Info(
        title = "Mobile Royal Stats Server",
        description = "Serverova Cast nasho mobilneho programu",
        version = "1.0.0",
        contact = @Contact(name = "Filip Agh & Martin Civan")),
        securityDefinition =
        @SecurityDefinition(apiKeyAuthDefinitions = @ApiKeyAuthDefinition(key = HttpHeaders.AUTHORIZATION , name="Authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER))
)
@ApplicationPath("/rest")
public class TestBean extends Application {

    /*@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ApieeService.class);
        return classes;
    }*/

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
