package utils

import javax.ws.rs.NameBinding



//@NameBinding
//@Retention(AnnotationRetention.RUNTIME)
//@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
//annotation class Secured


@NameBinding
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class Secured(vararg val value: Role = arrayOf())

enum class Role
{
    Leader,
    Coleader,
    User
}

