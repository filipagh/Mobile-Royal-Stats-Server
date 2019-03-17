import model.User
import utils.auth.*
import javax.annotation.Resource
import javax.ejb.EJB
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/u")
open class AuthenticationEndpoint {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager


    @Resource
    private lateinit var userTransaction: UserTransaction

    @EJB
    private lateinit var auth : AuthUtilsI

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

        if (username.isEmpty() or password.isEmpty())
        {
            throw Exception("name or pass is empty")
        }

        // pokusime sa najst usera
        val select = "Select u from User u WHERE u.name = \'$username\'"
        val response = manager.createQuery(select)
        val resultList = response.resultList


        for (item in resultList!!)
        {
            val dbUser: User = item as User
            if(auth.validatePassword(password,dbUser))
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
        user.role=Role.User
        auth.createUserPasswordHash(user)
        auth.createUserApiKey(user)

        // ulozime usra do db a akceptujeme ho
        userTransaction.begin()
        manager.persist(user)
        userTransaction.commit()

        return user
    }
}
