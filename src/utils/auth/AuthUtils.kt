package utils.auth

import model.Clan
import model.User
import org.mindrot.jbcrypt.BCrypt
import javax.annotation.Resource
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction

@Stateless
open class AuthUtils : AuthUtilsI {

    @PersistenceContext(unitName = "sql")
    private lateinit var manager: EntityManager

    @Resource
    private lateinit var userTransaction: UserTransaction

/*
 ukazka ako zabezpecit endpoint

@GET

    //         zadat role od akej urovne je pristupne (kuk) Role
@Secured(Role.Leader)

@Path("/info/{id}")
@Produces(MediaType.APPLICATION_JSON)

    //         ako parameter treba zadat @HeaderParam("Authorization") apiKey: String
open fun info(@PathParam("id") id: String,@HeaderParam("Authorization") apiKey: String): String {

    // Extract the token from the Authorization header
    val token = apiKey.substring(AUTHENTICATION_SCHEME.length).trim()



}
*/

    override fun removeUserFromClan(user: User) {
        var clan: Clan = manager.find(Clan::class.java, user.clanID)
        userTransaction.begin()
        if (clan.users.remove(user)) {
            user.clanID = null
            manager.persist(clan)
            manager.persist(user)
        }
        userTransaction.commit()
    }


    override fun findUserByApiKey(apiKey: String): User {

        val select = "Select u from User u WHERE u.apiKey = \'$apiKey\'"
        val response = manager.createQuery(select)
        val result = response.getResultList()
        if (result!!.size > 1) {
            throw Exception("we find duplicity apikey. please generate new (auth)")
        }
        return result[0] as User
    }

    override fun findClanByClanKey(apiKey: String): User {

        val select = "Select u from User u WHERE u.apiKey = \'$apiKey\'"
        val response = manager.createQuery(select)
        val result = response.getResultList()
        if (result!!.size > 1) {
            throw Exception("we find duplicity apikey. please generate new (auth)")
        }
        return result[0] as User
    }


    override fun checkUserRolePermission(apiKey: String, allowedRoles: List<Role>): Boolean {
        val user = findUserByApiKey(apiKey)
        return user.role in allowedRoles
    }


    override fun createUserPasswordHash(user: User) : User {
        //TODO otestovt null pass aj ""
        if (user.password.isNullOrEmpty()) {
            throw Exception("user password is empty!!!")
        }
        user.salt = BCrypt.gensalt()
        user.password = BCrypt.hashpw(user.password, user.salt)
        return user
    }

    override fun createUserApiKey(user: User) : User {
        //TODO kontrola validnosti casova api kluca

        if (user.apiKey.isNullOrEmpty()) {
            user.apiKey = BCrypt.gensalt(15)
        }
        return user
    }


    override fun createPasswordHash(password: String, salt: String): String {
        //TODO otestovt null pass aj ""
        if (password.isEmpty()) {
            throw Exception("user password is empty!!!")
        }
        return BCrypt.hashpw(password, salt)
    }

    override fun validatePassword(password: String, user: User): Boolean {
        //TODO otestovt null pass aj ""
        if (user.password.isNullOrEmpty()) {
            throw Exception("user password is empty!!!")
        }
        return BCrypt.hashpw(password, user.salt).equals(user.password)
    }

}



