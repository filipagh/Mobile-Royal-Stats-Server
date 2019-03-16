package utils

import java.io.Serializable
import java.lang.Exception
import java.util.*
import javax.annotation.Resource
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.UserTransaction

@PersistenceContext(unitName = "sql")
private lateinit var manager: EntityManager

@Resource
private lateinit var userTransaction: UserTransaction

fun dbSave(item: Serializable)
{
    try {
        userTransaction.begin()
        manager.persist(item)
        userTransaction.commit()
    } catch (e: Exception)
    {
        userTransaction.rollback()
        throw Exception("${item.toString()} IS NOT SUPPORTED ENTITY")
    }

}

fun dbJPAQuery(query: String): MutableList<Any?>? {
        val response = manager.createQuery(query)
        val resultList = response.getResultList()
        return resultList
}