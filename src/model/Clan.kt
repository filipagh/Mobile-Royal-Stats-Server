package model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name="clans")
class Clan : Serializable {

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "users_id")
    @SequenceGenerator(name = "clans_id", sequenceName = "clans_id_seq", allocationSize = 1)
    var id: Int? = null
    @NotEmpty
    @NotNull
    var name: String? = null
    @NotNull
    @NotEmpty
    var gameId: String? = null

    @OneToMany
    var users : List<User> = ArrayList()

    @OneToMany
    var conditions : List<Condition> = ArrayList()

}