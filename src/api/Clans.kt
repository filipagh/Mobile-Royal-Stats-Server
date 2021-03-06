package api

import api.views.ClanView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.*
import royal.ClanI
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
open class Clans {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager

    @Resource
    private lateinit var userTransaction: UserTransaction

    @EJB
    private lateinit var auth : AuthUtilsI

    @EJB
    private lateinit var clan: ClanI

    @ApiOperation(value = "Get Clan Players", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/players")
    open fun info(tag : Tag): ClanStats {
        val clanStats = jsonToClanStats(clan.loadClanStats(tag.tag.toString()))
        return clanStats
    }


    @ApiOperation(value = "Create new clan ", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    @Secured(Role.Admin, Role.User)
    open fun create(clanView : ClanView, @HeaderParam("Authorization") apiKey: String) : ClanView {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val clan = clanView.toClan(manager)
        val violations = validator.validate(clan)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }

        val user = auth.findUserByApiKey(apiKey)
        clan.users.add(user)
        clan.id = null
        user.role = Role.Leader
        userTransaction.begin()
        manager.persist(clan)
        user.clan = clan
        manager.merge(user)
        userTransaction.commit()

        return ClanView(clan)
    }

    @ApiOperation(value = "Delete clan ", notes = "ID is ignored (should be removed)")
    @DELETE
    @Path("/")
    @Secured(Role.Admin, Role.Leader)
    open fun delete(@HeaderParam("Authorization") apiKey: String) : Response {
        userTransaction.begin()
        val user = auth.findUserByApiKey(apiKey)
        //val clan: Clan? = manager.find(Clan::class.java, user.clan)
        if (user.clan == null) {
            userTransaction.commit()
            return Response.status(Response.Status.NOT_FOUND).build()
        }
        val clan = user.clan
        user.role = Role.User
        user.clan = null
        manager.merge(user)
        manager.remove(clan)
        userTransaction.commit()
        return Response.ok().build()
    }

    @ApiOperation(value = "Get user clan ", notes = "ID is ignored (should be removed)")
    @GET
    @Path("/")
    @Secured(Role.Admin, Role.Leader, Role.User, Role.Coleader)
    open fun myClan(@HeaderParam("Authorization") apiKey: String) : ClanView {
        val select = "Select u from User u JOIN FETCH u.clan c LEFT JOIN FETCH c.users LEFT JOIN FETCH c.conditions WHERE u.apiKey = \'$apiKey\' "
        val response = manager.createQuery(select)
        val result = response.resultList
        if (result.isEmpty()) {
            throw ApiException(404, "Clan not found")
        }
        val user = result[0] as User
        if (user.clan == null) {
            throw ApiException(404, "Clan not found")
        }
        return ClanView(user.clan)
    }


    @ApiOperation(value = "Join Clan", notes = "ID is ignored (should be removed)")
    @POST
    @Secured(Role.Admin, Role.User)
    @Path("/token")
    open fun joinClan(clanView : ClanView, @HeaderParam("Authorization") apiKey: String) : ClanView {
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val clan = clanView.toClan(manager)
        val violations = validator.validate(clan)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }


        val user: User = auth.findUserByApiKey(apiKey)
        //TODO overenie klanu
        if (user.clan != null) {
            throw ApiException(400, "User is in clan ${clan.name}")
        }
        var joinClan = loadClanByToken(clan.token!!, manager)

        if (joinClan == null) {
            throw ApiException(404, "Clan not found ${clan.name}")
        }

        //TODO  ZISTIT gameID hracov z royalapi z klanu a overit ci user ma take gameID

        joinClan.users.add(user)
        user.clan = joinClan

        userTransaction.begin()
        manager.merge(joinClan)
        manager.merge(user)
        userTransaction.commit()
        return ClanView(joinClan)
    }
}