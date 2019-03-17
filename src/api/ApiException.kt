package api

import java.io.Serializable


class ApiException(val code: Int, msg : String) : Exception(msg), Serializable {
    override fun toString(): String {
        return message ?: ""
    }
}