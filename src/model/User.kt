package model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name="users")
class User : Serializable {

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "users_id")
    @SequenceGenerator(name = "users_id", sequenceName = "users_id_seq", allocationSize = 1)
    var id: Int? = null
    var name: String? = null

    @Email
    @NotEmpty
    @NotNull
    var email: String? = null

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be null")
    var password: String? = null
    var salt: String? = null
    var apiKey: String? = null

}