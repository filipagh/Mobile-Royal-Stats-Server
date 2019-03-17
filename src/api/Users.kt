package api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.User
import utils.auth.Auth
import utils.auth.Role
import javax.annotation.Resource
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction
import javax.validation.Validation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


@Api(value = "Users")
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Users {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager

    @Resource
    private lateinit var userTransaction: UserTransaction

    @GET
    @Path("/sayHello")
    open fun sayHello(): String {

        return "{\"nieco\": \"ine\"}"
    }

    @ApiOperation(value = "Create new user (registration)", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    open fun create(user : User) : User {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(user)
        if (!violations.isEmpty()) {
           throw ApiException(400, "ZLE")
        }

        user.id = null
        //TODO setovat spravnu rolu
        user.role= Role.User
        val auth = Auth()
        auth.authCreateUserPasswordHash(user)
        auth.authCreateUserApiKey(user)

        // ulozime usra do db a akceptujeme ho
//        try {
            userTransaction.begin()
            manager.persist(user)
            userTransaction.commit()
//        } catch (e: Exception) {
//            throw ApiException(400,"Duplicate entry")
//        }
        // fild co nechceme vracat
        user.salt=null
        user.password=null
        return user
    }


}
