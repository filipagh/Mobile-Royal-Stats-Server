import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam

@Path("/hraci")
class Hraci {
    @GET
    @Path("/sayHello")
    fun sayHello(): String {
        return "<h1>Hello World</h1>"
    }

    @GET
    @Path("/ids/{id}")
    fun info(@PathParam("id") id : Int): String {
        return "<h1>Hello World $id</h1>"
    }
}
