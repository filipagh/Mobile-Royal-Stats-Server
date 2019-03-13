package model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name="users")
class User : Serializable {

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "users_id")
    @SequenceGenerator(name = "users_id", sequenceName = "users_id_seq", allocationSize = 1)
    var id: Int? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var salt: String? = null
    var apiKey: String? = null

}