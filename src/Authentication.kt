import model.User
import utils.auth.*
import utils.dbJPAQuery
import utils.dbSave
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/u")
open class AuthenticationEndpoint {

    @POST
    @Path("/authentication")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
   open fun authenticateUser(@HeaderParam("username") username: String,
                             @HeaderParam("password") password: String): Response {

        lateinit var user: User

        try {

            // Authenticate the user using the credentials provided
            user = authenticate(username, password)

        } catch (e: Exception) {
            return Response.status(Response.Status.FORBIDDEN).build()
        }

        // Issue a token for the user
        val token = user.apiKey

        // Return the token on the response
        return Response.ok(token).build()
    }

    /**
     *    // Authenticate against a database, LDAP, file or whatever
     *    // Throw an Exception if the credentials are invalid
     */
    @Throws(Exception::class)
    private fun authenticate(username: String, password: String): User {

        if (username.isEmpty() and password.isEmpty())
        {
            throw Exception("name or pass is empty")
        }

        // pokusime sa najst usera
        val select = "Select u from User u WHERE u.name = \'$username\'"
        val resultList = dbJPAQuery(select)

        for (item in resultList!!)
        {
            val dbUser: User = item as User
            if(authValidatePassword(password,dbUser))
            {
                return dbUser  // we find user and password is OK (auth OK)
            }
        }

        // usera sme nenasli tak ho vytvorime
        val user = User()
        user.id = null
        user.name = username
        user.password=password
        //TODO setovat spravnu rolu
        user.role=Role.Leader
        authCreateUserPasswordHash(user)
        authCreateUserApiKey(user)

        // ulozime usra do db a akceptujeme ho
        dbSave(user)

        return user
    }
}
