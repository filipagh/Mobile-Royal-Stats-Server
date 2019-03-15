package utils

import com.google.common.net.HttpHeaders
import javax.ws.rs.Priorities
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.core.Context


import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ResourceInfo
import javax.annotation.security.PermitAll
import javax.annotation.security.RolesAllowed
import javax.annotation.security.DenyAll
import java.io.IOException

import javax.ws.rs.ext.Provider
import javax.annotation.Priority
import java.util.Arrays
import java.util.ArrayList
import java.lang.reflect.AnnotatedElement
import netscape.security.Privilege.FORBIDDEN
import java.awt.Event
import javax.inject.Inject
import javax.ws.rs.core.Response



@Inject
@AuthenticatedUser
var userAuthenticatedEvent: Event? = null

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
open class AuthorizationFilter: ContainerRequestFilter {



    @Context
    private val resourceInfo: ResourceInfo? = null

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext) {

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
                checkPermissions(classRoles)
            } else {
                checkPermissions(methodRoles)
            }

           // userAuthenticatedEvent.fire(username);

        } catch (e: Exception) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).build())
        }

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
    private fun checkPermissions(allowedRoles: List<Role>) {

        //userAuthenticatedEvent.fire(username);

        //throw Exception()
        // Check if the user contains one of the allowed roles
        // Throw an Exception if the user has not permission to execute the method
    }
}

//TODO https://stackoverflow.com/questions/26777083/best-practice-for-rest-token-based-authentication-with-jax-rs-and-jersey
//TODO https://crackstation.net/hashing-security.htm
