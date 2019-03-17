package utils.auth

import model.User
import org.mindrot.jbcrypt.BCrypt
import javax.persistence.EntityManager
import javax.persistence.Persistence

open class Auth {


    private var manager: EntityManager

    init
    {
        val entityManagerFactory = Persistence.createEntityManagerFactory("sql")
        manager = entityManagerFactory.createEntityManager()
    }

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


//        val a = securityContext.userPrincipal.name
    return "<h1>$token</h1>"
    return hraci.dajHraca(id)

}
*/



    private fun findUserByApiKey(apiKey: String): User {

        val select = "Select u from User u WHERE u.apiKey = \'$apiKey\'"
        val response = manager.createQuery(select)
        val result = response.getResultList()
        if (result!!.size > 1) {
            throw Exception("we find duplicity apikey. please generate new (auth)")
        }
        return result[0] as User
    }


    fun authCheckUserRolePermission(apiKey: String, allowedRoles: List<Role>): Boolean {
        val user = findUserByApiKey(apiKey)
        val pass = user.role in allowedRoles
        return pass
    }


    fun authCreateUserPasswordHash(user: User) {
        //TODO otestovt null pass aj ""
        if (user.password.isNullOrEmpty()) {
            throw Exception("user password is empty!!!")
        }
        user.salt = BCrypt.gensalt()
        user.password = BCrypt.hashpw(user.password, user.salt)
    }

    fun authCreateUserApiKey(user: User) {
        //TODO kontrola validnosti casova api kluca

        if (user.apiKey.isNullOrEmpty()) {
            user.apiKey = BCrypt.gensalt(15)
        }
    }


    fun createPasswordHash(password: String, salt: String): String {
        //TODO otestovt null pass aj ""
        if (password.isNullOrEmpty()) {
            throw Exception("user password is empty!!!")
        }
        return BCrypt.hashpw(password, salt)
    }

    fun authValidatePassword(password: String, user: User): Boolean {
        //TODO otestovt null pass aj ""
        if (user.password.isNullOrEmpty()) {
            throw Exception("user password is empty!!!")
        }
        return BCrypt.hashpw(password, user.salt).equals(user.password)
    }

}



