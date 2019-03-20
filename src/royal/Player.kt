package royal

import java.net.URL
import javax.ejb.Stateless

@Stateless
open class Player : PlayerI {
    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjM0MSwiaWRlbiI6IjMxOTU1NTA0ODMzMTgwNDY3MiIsIm1kIjp7fSwidHMiOjE1NTAwNzAzOTk0MjV9.XSdL-1eeDccp5bUsLayZgml1LBjqvxVel71AU3rxQZw"
    val url = "https://api.royaleapi.com/player/"

    override fun dajHraca(id : String) : String {
        val con = URL(url + id).openConnection()
        con.setRequestProperty("auth", apiKey)
        lateinit var  result:StringBuffer
        try {
            var rider =  con.getInputStream().bufferedReader()
            result = StringBuffer()
            do {
                val line = rider.readLine()
                if(line != null) {
                    result.append(line)
                }
            } while (line != null)
        } catch (e: Exception)
        {
            throw java.lang.Exception ("NIECO SA POKASLALO S APIROYAL "+ e +" "+ e.stackTrace)
        }


        return result.toString()
    }
}