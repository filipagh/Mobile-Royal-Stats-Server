package api

import org.apache.commons.lang3.RandomStringUtils
import javax.ejb.Stateless
import javax.jws.WebService

@Stateless
@WebService
open class Echo
{

    open fun generujLogin(): String {
     val login = RandomStringUtils.random(10, true, true)
        return login
    }

    open fun generujHeslo(): String {
        val heslo = RandomStringUtils.random(10, true, true)
        return heslo
    }

    open fun echo():String  {
     return "aaaa"
    }
}
