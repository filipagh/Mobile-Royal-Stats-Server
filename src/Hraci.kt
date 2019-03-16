import royal.PlayerI
import utils.auth.Role
import utils.auth.Secured
import javax.ejb.EJB
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


@Secured
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
    @Secured(Role.Admin,Role.Leader,Role.Coleader,Role.User)
    @Path("/sayHelloUser")
    open fun sayHelloooo(): String {
        return "<h1>som aspon User</h1>"
    }

    @GET
    @Secured(Role.Admin)
    @Path("/sayHelloAdmin")
    open fun sayHelloo(): String {
        return "<h1>som aspon admin</h1>"
    }

    @GET
    @Secured(Role.Admin,Role.Leader,Role.Coleader)
    @Path("/sayHelloColeader")
    open fun sayHellooo(): String {
        return "<h1>som aspon coleader</h1>"
    }

    @GET
    @Path("/info/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    open fun info(@PathParam("id") id: String): String {

        return hraci.dajHraca(id)
    }
}



