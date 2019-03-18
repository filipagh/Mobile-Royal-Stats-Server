package api.views

import model.Clan
import model.User
import javax.persistence.EntityManager

open class UserView(user : User) {
    var id = user.id
    var name = user.name
    var email = user.email
    var gameId = user.gameId
    var apiKey = user.apiKey
    var role = user.role
    var clanId = user.clan?.id

    fun toUser(manager: EntityManager): User {
        val user = User()
        user.role = role
        user.apiKey = apiKey
        user.email = this.email
        user.gameId = gameId
        user.role = role
        user.name = name
        val clan = manager.find(Clan::class.java, clanId)
        user.clan = clan

        return user
    }
}