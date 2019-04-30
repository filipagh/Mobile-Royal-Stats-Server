package model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * entita clanu
 */
@Entity
class Clan : Serializable {

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    @NotEmpty
    @NotNull
    var name: String? = null
    @NotNull
    @NotEmpty
    var gameId: String? = null

    @OneToMany()
    var users : MutableSet<User> = HashSet()

    @OneToMany
    var conditions : MutableSet<Condition> = HashSet()

    @NotNull
    @NotEmpty
    var token : String? = null
}

fun loadClanByID(id: Int, manager: EntityManager): Clan? {
    //      val selectt = "Select u from User u JOIN FETCH u.clan c LEFT JOIN FETCH c.users LEFT JOIN FETCH c.conditions WHERE u.apiKey = \'$apiKey\' "
    val select = "SELECT c FROM Clan c LEFT JOIN FETCH c.users LEFT JOIN FETCH c.conditions WHERE c.id = \'$id\'"
    val response = manager.createQuery(select)
    val result = response.resultList
    return result[0] as Clan
}

fun loadClanByToken(token: String,manager: EntityManager): Clan? {
    //      val selectt = "Select u from User u JOIN FETCH u.clan c LEFT JOIN FETCH c.users LEFT JOIN FETCH c.conditions WHERE u.apiKey = \'$apiKey\' "
    val select = "SELECT c FROM Clan c LEFT JOIN FETCH c.users LEFT JOIN FETCH c.conditions WHERE c.token = \'$token\'"
    val response = manager.createQuery(select)
    val result = response.resultList
    return result[0] as Clan
}