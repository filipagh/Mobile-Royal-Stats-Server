package utils.auth

import model.User
import javax.ejb.Local

@Local
interface AuthUtilsI {
    fun checkUserRolePermission(apiKey: String, allowedRoles: List<Role>): Boolean
    fun createUserPasswordHash(user: User) : User
    fun createUserApiKey(user: User) : User
    fun createPasswordHash(password: String, salt: String): String
    fun validatePassword(password: String, user: User): Boolean
    fun findUserByApiKey(apiKey: String): User
}