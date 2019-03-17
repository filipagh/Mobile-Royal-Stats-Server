package api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.Clan
import model.User
import javax.annotation.Resource
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction
import javax.validation.Validation
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Api(value = "Clans")
@Path("/clans")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Clans {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager
//    @PersistenceContext(unitName = "sql")
//    private lateinit var session: Session

    @Resource
    private lateinit var userTransaction: UserTransaction

    @ApiOperation(value = "Create new clan ", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    open fun create(clan : Clan) : Clan {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(clan)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }
        clan.id = null
        userTransaction.begin()
        manager.persist(clan)
        userTransaction.commit()
        return clan
    }

    @ApiOperation(value = "Delete clan ", notes = "ID is ignored (should be removed)")
    @DELETE
    @Path("/{id}")
    open fun delete(@PathParam("id") id : Int) : Response {
        val clan: Clan = manager.find(Clan::class.java, id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        userTransaction.begin()
        manager.remove(clan)
        userTransaction.commit()
        return Response.ok().build()
    }

}