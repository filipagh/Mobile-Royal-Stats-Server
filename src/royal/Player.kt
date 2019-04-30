package royal

import utils.PropertiesManagerI
import java.net.URL
import java.util.logging.Logger
import javax.ejb.EJB
import javax.ejb.Stateless

@Stateless
open class Player : PlayerI {
    private val log = Logger.getLogger(Player::class.java.name)
    private val url = "https://api.royaleapi.com/player/"

    @EJB
    private lateinit var prop: PropertiesManagerI

    override fun dajHraca(id: String): String {
        val royalApiKey = prop.getProp("royalApiKey")

        val con = URL(url + id).openConnection()
        con.setRequestProperty("auth", royalApiKey)
        lateinit var result: StringBuffer
        try {
            val rider = con.getInputStream().bufferedReader()
            result = StringBuffer()
            do {
                val line = rider.readLine()
                if (line != null) {
                    result.append(line)
                }
            } while (line != null)
        } catch (e: Exception) {
            log.warning("NIECO SA POKASLALO S APIROYAL " + e.printStackTrace())
            //throw java.lang.Exception("NIECO SA POKASLALO S APIROYAL " + e + " " + e.stackTrace)
        }

        return result.toString()
    }
}