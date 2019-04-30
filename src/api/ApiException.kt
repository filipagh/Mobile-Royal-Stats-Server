package api

import java.io.Serializable

/**
 * nasa vlastna api exception
 */
class ApiException(val code: Int, msg : String) : Exception(msg), Serializable {
    override fun toString(): String {
        return message ?: ""
    }
}