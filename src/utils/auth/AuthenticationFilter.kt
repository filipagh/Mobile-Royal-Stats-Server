package utils.auth


import com.google.common.net.HttpHeaders
import java.io.IOException
import java.lang.reflect.AnnotatedElement
import java.util.*
import javax.annotation.Priority
import javax.ws.rs.Priorities
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.ext.Provider

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
open class AuthorizationFilter: ContainerRequestFilter {

//    @Context
//    open var securityContext: SecurityContext? = null


    @Context
    private val resourceInfo: ResourceInfo? = null

//TODO



    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext) {

        val authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION)

        if (authorizationHeader.isNullOrEmpty()) {
            abortWithUnauthorized(requestContext)
        }

        // Extract the token from the Authorization header
        val token = authorizationHeader.trim()

        // Get the resource class which matches with the requested URL
        // Extract the roles declared by it
        val resourceClass = resourceInfo!!.resourceClass
        val classRoles = extractRoles(resourceClass)

        // Get the resource method which matches with the requested URL
        // Extract the roles declared by it
        val resourceMethod = resourceInfo.resourceMethod
        val methodRoles = extractRoles(resourceMethod)

        try {

            // Check if the user is allowed to execute the method
            // The method annotations override the class annotations
            if (methodRoles.isEmpty()) {
                checkPermissions(classRoles,token)
            } else {
                checkPermissions(methodRoles,token)
            }

           // userAuthenticatedEvent.fire(username);

        } catch (e: Exception) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build())
        }

    }


    private fun isTokenBasedAuthentication(authorizationHeader: String?): Boolean {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null
    }

    private fun abortWithUnauthorized(requestContext: ContainerRequestContext) {

        // request neobsahuje nas API token
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED).build())
    }

    // Extract the roles from the annotated element
    private fun extractRoles(annotatedElement: AnnotatedElement?): List<Role> {
        if (annotatedElement == null) {
            return ArrayList()
        } else {
            val secured = annotatedElement.getAnnotation(Secured::class.java)
            if (secured == null) {
                return ArrayList()
            } else {
                val allowedRoles = secured.value
                return Arrays.asList(*allowedRoles)
            }
        }
    }

    @Throws(Exception::class)
    private fun checkPermissions(allowedRoles: List<Role>,apiKey: String) {

        // neviem preco to chyti auth aj ked endpoint je bez @secure
        if (allowedRoles.isEmpty())
        {
            return
        }

        val auth = Auth()
        if (!auth.authCheckUserRolePermission(apiKey,allowedRoles))
        {
            throw Exception()
        }

        //throw Exception()
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
    }
}


//TODO https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
//TODO https://crackstation.net/hashing-security.htm
