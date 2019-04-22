package api

import api.views.ConditionView
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.Clan
import model.Condition
import model.User
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


@Api(value = "Conditions")
@Path("/clans/conditions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Conditions {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager
//    @PersistenceContext(unitName = "sql")
//    private lateinit var session: Session

    @Resource
    private lateinit var userTransaction: UserTransaction

    @EJB
    private lateinit var auth : AuthUtilsI

    @ApiOperation(value = "List condition", notes = "ID is ignored (should be removed)")
    @GET
    @Secured(Role.Admin, Role.Coleader, Role.Leader, Role.User)
    @Path("/")
    open fun list(@HeaderParam("Authorization") apiKey: String): Set<ConditionView> {
        val select = "Select co from User u LEFT JOIN u.clan c LEFT JOIN c.conditions co WHERE u.apiKey = \'$apiKey\' "
        val response = manager.createQuery(select)
        val result = response.resultList
        if (result.isEmpty()) {
            return HashSet<ConditionView>()
        }
        val r = result.map { row -> ConditionView(row as Condition) }.toHashSet()
        return r
    }

    @ApiOperation(value = "Create new condition", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/")
    @Secured(Role.Coleader, Role.Leader, Role.Admin)
    open fun create(conditionView : ConditionView, @HeaderParam("Authorization") apiKey: String) : ConditionView {
        val select = "Select u from User u JOIN FETCH u.clan c LEFT JOIN FETCH c.conditions WHERE u.apiKey = \'$apiKey\' "
        val response = manager.createQuery(select)
        val result = response.resultList
        if (result.isEmpty()) {
            throw ApiException(404, "Clan not found")
        }
        val user = result[0] as User
        val condition = conditionView.toCondition(manager)
        if (user.clan == null || user.clan!!.id == null) {
            throw ApiException(403, "Neautorizovany")
        }
        condition.clan = user.clan
        condition.id = null
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(condition)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }
        userTransaction.begin()
        manager.persist(condition)
        user.clan!!.conditions.add(condition)
        manager.merge(user.clan)
        userTransaction.commit()
        return ConditionView(condition)
    }

    @ApiOperation(value = "Modify condition", notes = "ID is ignored (should be removed)")
    @PUT
    @Path("/")
    @Secured(Role.Coleader, Role.Leader, Role.Admin)
    open fun update(conditionView : ConditionView, @HeaderParam("Authorization") apiKey: String) : ConditionView {
        val select = "Select u from User u JOIN FETCH u.clan c LEFT JOIN FETCH c.conditions WHERE u.apiKey = \'$apiKey\' "
        val response = manager.createQuery(select)
        val result = response.resultList
        if (result.isEmpty()) {
            throw ApiException(404, "Clan not found")
        }
        val user = result[0] as User
        val condition = conditionView.toCondition(manager)
        if (user.clan == null || user.clan!!.id == null) {
            throw ApiException(403, "Unauthorized")
        }
//        if (user.clan!!.id != condition.clan!!.id) {
//            throw ApiException(403, "Unauthorized")
//        }
        condition.clan = user.clan
        val factory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator
        val violations = validator.validate(condition)
        if (!violations.isEmpty()) {
            throw ApiException(400, "ZLE")
        }
        userTransaction.begin()
        manager.merge(condition)
        userTransaction.commit()
        return ConditionView(condition)
    }


    @ApiOperation(value = "Delete condition", notes = "sad")
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/{id}")
    open fun delete(@PathParam("id") id : Int, @HeaderParam("Authorization") apiKey: String) : Response {
        val user = auth.findUserByApiKey(apiKey)
        if (user.clan == null) {
            throw ApiException(403, "Unauthorized")
        }
        userTransaction.begin()
        val condition: Condition = manager.find(Condition::class.java, id) ?: return Response.status(Response.Status.NOT_FOUND).build()
        if (user.clan!!.id != condition.clan!!.id) {
            userTransaction.commit()
            throw ApiException(403, "Unauthorized")
        }
        val clan : Clan = manager.find(Clan::class.java, condition.clan!!.id)
        clan.conditions.remove(condition)
        manager.merge(clan)
        manager.remove(condition)
        userTransaction.commit()
        return Response.ok().build()
    }

    @ApiOperation(value = "Get condition", notes = "sad")
    @GET
    @Path("/{id}")
    open fun get(@PathParam("id") id : Int, @HeaderParam("Authorization") apiKey: String) : ConditionView {
        val user = auth.findUserByApiKey(apiKey)
        if (user.clan == null) {
            throw ApiException(403, "Unauthorized")
        }
        userTransaction.begin()
        val condition: Condition = manager.find(Condition::class.java, id) ?: throw ApiException(404, "Not found")
        if (user.clan!!.id != condition.clan!!.id) {
            userTransaction.commit()
            throw ApiException(403, "Unauthorized")
        }

        userTransaction.commit()
        return ConditionView(condition)
    }

}
