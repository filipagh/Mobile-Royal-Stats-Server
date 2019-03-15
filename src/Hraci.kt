import royal.PlayerI
import utils.Role
import utils.Secured
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.ejb.EJB
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import netscape.security.Privilege.FORBIDDEN
import javax.ws.rs.FormParam
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.core.Response


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
    @Secured(Role.Leader)
    @Path("/info/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    open fun info(@PathParam("id") id: String): String {
        return hraci.dajHraca(id)

    }
}





//@Path("/authentication")
//class AuthenticationEndpoint {
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    fun authenticateUser(@FormParam("username") username: String,
//                         @FormParam("password") password: String): Response {
//
//        try {
//
//            // Authenticate the user using the credentials provided
//            authenticate(username, password)
//
//            // Issue a token for the user
//            val token = issueToken(username)
//
//            // Return the token on the response
//            return Response.ok(token).build()
//
//        } catch (e: Exception) {
//            return Response.status(Response.Status.FORBIDDEN).build()
//        }
//
//    }
//
//    @Throws(Exception::class)
//    private fun authenticate(username: String, password: String) {
//        // Authenticate against a database, LDAP, file or whatever
//        // Throw an Exception if the credentials are invalid
//    }
//
//    private fun issueToken(username: String): String {
//        // Issue a token (can be a random String persisted to a database or a JWT token)
//        // The issued token must be associated to a user
//        // Return the issued token
//    }
//}
