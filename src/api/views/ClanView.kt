package api.views

import model.Clan
import model.Condition
import model.User
import javax.persistence.EntityManager

open class ClanView(clan: Clan? = null) {
    var id = clan?.id
    var name = clan?.name
    var gameId = clan?.gameId
    var token = clan?.token
    var userIds: Set<Int?>? = clan?.users?.map { user -> user.id }?.toSet()
    var conditionIds = clan?.conditions?.map { condition -> ConditionView(condition) }?.toSet()

    fun toClan(manager: EntityManager) :Clan {
        val clan = Clan()
        clan.id = id
        clan.name = name
        clan.gameId = gameId
        clan.token = token

        if (!userIds.isNullOrEmpty()) {
            val select = manager.createQuery("Select u from User u WHERE u.id IN (:ids)")
            select.setParameter("ids", userIds)
            clan.users = (select.resultList as MutableList<User>).toHashSet()
        }

        if (!conditionIds.isNullOrEmpty()) {
            val select = manager.createQuery("Select u from Condition u WHERE u.id in (:ids)")
            select.setParameter("ids", conditionIds)
            clan.conditions = (select.resultList as MutableList<Condition>).toHashSet()
        }

        return clan
    }
}