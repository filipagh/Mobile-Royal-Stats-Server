package api

import javax.ejb.Stateless
import javax.jws.WebService

@Stateless
@WebService
open class Echo
{

    open fun sayHello(name: String): String {
        return  name
    }

    open fun echo():String  {
     return "aaaa"
    }
}
