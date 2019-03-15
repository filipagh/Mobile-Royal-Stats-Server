package utils

import model.User
import java.lang.Exception
import javax.enterprise.context.RequestScoped
import javax.enterprise.event.Observes
import javax.enterprise.inject.Produces


@RequestScoped
class AuthenticatedUserProducer {

    @Produces
    @RequestScoped
    @AuthenticatedUser
    private var authenticatedUser: User? = null

    fun handleAuthenticationEvent(@Observes @AuthenticatedUser username: String) {
        this.authenticatedUser = findUser(username)
    }

    private fun findUser(username: String): User {
        // Hit the the database or a service to find a user by its username and return it
        // Return the User instance
        throw Exception()
    }
}