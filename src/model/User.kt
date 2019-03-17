package model

import utils.auth.Role
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name= "users")
class User : Serializable {

    companion object {
        val DB_NAME = "user"
    }

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "users_id")
    @SequenceGenerator(name = "users_id", sequenceName = "users_id_seq", allocationSize = 1)
    var id: Int? = null
    var name: String? = null
    var gameId: String? = null
    var role: String? = null

//    @UniqueConstraint(name="email")
    @Email
    @NotEmpty
    @NotNull
    lateinit var email: String

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be null")
    var password: String? = null
    var salt: String? = null
    var apiKey: String? = null
    var role: Role? = null

}

