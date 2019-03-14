import royal.PlayerI
import javax.ejb.EJB
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/hraci")
open class Hraci {

    @EJB


    private lateinit var hraci: PlayerI

    @GET
    @Path("/sayHello")
    open fun sayHello(): String {
        return "<h1>Hello World</h1>"
    }

    @GET
    @Path("/info/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    open fun info(@PathParam("id") id: String): String {
        return hraci.dajHraca(id)

    }
}
