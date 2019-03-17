package api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.Clan
import utils.auth.AuthUtilsI
import utils.auth.Role
import utils.auth.Secured
import javax.annotation.Resource
import javax.ejb.EJB
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
@Secured(Role.Admin, Role.Coleader, Role.Leader, Role.User)
open class Clans {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager
//    @PersistenceContext(unitName = "sql")
//    private lateinit var session: Session

    @Resource
    private lateinit var userTransaction: UserTransaction

    @EJB
    private lateinit var auth : AuthUtilsI

    @ApiOperation(value = "Create new clan ", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    open fun create(clan : Clan, @HeaderParam("Authorization") apiKey: String) : Clan {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(clan)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }

        val user = auth.findUserByApiKey(apiKey)
        clan.users.add(user)
        user.role = Role.Leader
        clan.id = null
        userTransaction.begin()
        manager.persist(clan)
        manager.persist(user)
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