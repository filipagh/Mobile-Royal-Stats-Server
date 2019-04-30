package api

import api.views.UserView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.User
import utils.auth.AuthUtilsI
import utils.auth.Role
import javax.annotation.Resource
import javax.ejb.EJB
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.RollbackException
import javax.transaction.UserTransaction
import javax.validation.Validation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

/**
 * rest api volania
 */
@Api(value = "Users")
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Users {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager

    @Resource
    private lateinit var userTransaction: UserTransaction

    @EJB
    private lateinit var auth : AuthUtilsI

    @GET
    @Path("/sayHello")
    open fun sayHello(): String {

        return "{\"nieco\": \"ine\"}"
    }

    @ApiOperation(value = "Login user", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/login")
    open fun login(user : User) : UserView {

        if (user.email.isEmpty() or user.password.isEmpty())
        {
            throw ApiException(400, "email or pass is empty")
        }

        val authenticated : User = auth.authenticateUser(user.email, user.password) ?: throw ApiException(401, "Wrong username/password")

        return UserView(authenticated)
    }

    @ApiOperation(value = "Create new user (registration)", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    open fun create(user : User) : UserView {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(user)
        if (!violations.isEmpty()) {
           throw ApiException(400, "ZLE")
        }

        user.id = null
        user.clan = null
        //TODO setovat spravnu rolu
        user.role= Role.User
        auth.createUserPasswordHash(user)
        auth.createUserApiKey(user)

        // ulozime usra do db a akceptujeme ho
        try {
            userTransaction.begin()
            manager.persist(user)
            userTransaction.commit()
        } catch (e: RollbackException) {
            throw ApiException(400,"Duplicate entry")
        }
        return UserView(user)
    }


}
