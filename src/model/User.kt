package model

import utils.auth.Role
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * entita usera
 */
@Entity
class User : Serializable {

    companion object {
        val DB_NAME = "user"
    }

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null
    var name: String? = null
    var gameId: String? = null
    var role: Role? = null
    @ManyToOne
    var clan: Clan? = null

    @Column(unique=true)
    @Email
    @NotEmpty
    @NotNull
    lateinit var email: String

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    var password: String = ""
    var salt: String? = null
    @Column(unique=true)
    var apiKey: String? = null
}

