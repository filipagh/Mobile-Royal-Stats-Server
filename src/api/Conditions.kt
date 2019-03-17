package api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.Clan
import model.Condition
import model.User
import javax.annotation.Resource
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction
import javax.validation.Validation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Api(value = "Conditions")
@Path("/clans/{clanId}/conditions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Conditions {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager
//    @PersistenceContext(unitName = "sql")
//    private lateinit var session: Session

    @Resource
    private lateinit var userTransaction: UserTransaction

    @GET
    @Path("/sayHello")
    open fun sayHello(): String {

        return "{\"nieco\": \"ine\"}"
    }

    @ApiOperation(value = "Create new condition", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    open fun create(@PathParam("clanId") clanId : Int, condition : Condition) : Condition {
        val clan: Clan = manager.find(Clan::class.java, clanId)
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(condition)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }
        userTransaction.begin()
        manager.persist(condition)
        userTransaction.commit()
        return condition
    }

    @ApiOperation(value = "Modify condition", notes = "ID is ignored (should be removed)")
    @PUT
    @Path("/{id}")
    open fun update(@PathParam("clanId") clanId : Int, @PathParam("id") id : Int, condition : Condition) : Condition {
        val clan: Clan = manager.find(Clan::class.java, clanId)
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(condition)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }
        userTransaction.begin()
        manager.persist(condition)
        userTransaction.commit()
        return condition
    }

    @ApiOperation(value = "Delete condition", notes = "sad")
    @DELETE
    @Path("/{id}")
    open fun delete(@PathParam("clanId") clanId : Int, @PathParam("id") id : Int) : Response {
        val clan: Clan = manager.find(Clan::class.java, clanId)
        val condition: Condition = manager.find(Condition::class.java, id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        userTransaction.begin()
        manager.remove(condition)
        userTransaction.commit()
        return Response.ok().build()
    }

}
