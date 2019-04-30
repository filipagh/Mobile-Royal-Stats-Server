package model

import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * entita na podmienka
 */
@Entity
@Table(name="conditions")
class Condition : Serializable {
//    @SequenceGenerator(name = "users_id", sequenceName = "users_id_seq", allocationSize = 1)
    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
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