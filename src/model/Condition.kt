package model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name="conditions")
class Condition : Serializable {

    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.AUTO, generator = "users_id")
    @SequenceGenerator(name = "users_id", sequenceName = "users_id_seq", allocationSize = 1)
    var id: Int? = null
    var name: String? = null
    @NotEmpty
    @NotNull
    var field: String? = null
    @NotEmpty
    @NotNull
    var operator: String? = null
    var value: String? = null
    @ManyToOne
    var clan : Clan? = null
}