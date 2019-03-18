package utils.auth

import model.User
import javax.ejb.Local

@Local
interface AuthUtilsI {
    @Throws(Exception::class)
    fun checkUserRolePermission(apiKey: String, allowedRoles: List<Role>): Boolean
    fun createUserPasswordHash(user: User) : User
    fun createUserApiKey(user: User) : User
    fun createPasswordHash(password: String, salt: String): String
    fun validatePassword(password: String, user: User): Boolean
    @Throws(Exception::class)
    fun findUserByApiKey(apiKey: String): User
    fun authenticateUser(email: String, password: String): User?
}