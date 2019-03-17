package api.views

import model.Condition
import javax.persistence.EntityManager

class ConditionView(condition: Condition? = null) {
    val name = condition?.name
    val id = condition?.id
    val field = condition?.field
    val operator = condition?.operator
    val value = condition?.value
    val clanId = condition?.clan?.id


    fun toCondition(manager: EntityManager) : Condition {
        val condition = Condition()
        condition.name = name
        condition.id = id
        condition.field = field
        condition.operator = operator
        condition.value = value
//        condition.clan = manager.find(Clan::class.java, clanId)

        return condition
    }
}