package utils.auth

import javax.ws.rs.NameBinding

const val ROLE_ADMIN = 10
const val ROLE_LEADER = 7
const val ROLE_COLEADER = 5
const val ROLE_USER = 2

@NameBinding
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class Secured(vararg val value: Role = arrayOf())


/**
 * zoznam roly pouzivatelov
 */
enum class Role
{
    Admin,
    Leader,
    Coleader,
    User
}



