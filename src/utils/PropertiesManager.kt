package utils

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*
import java.util.logging.Logger
import javax.annotation.PostConstruct
import javax.ejb.Singleton
import javax.ejb.Startup

/**
 * PropertiesManager
 */
@Startup
@Singleton
open class PropertiesManager : PropertiesManagerI {
    var prop = Properties()
    private val log = Logger.getLogger(PropertiesManager::class.java.name)
    /**
     *  nacitanie suboru do prop
     */
    @PostConstruct
    open fun initProperties() {
        var impStream: FileInputStream? = null
        try {
            impStream = FileInputStream(System.getProperty("jboss.server.config.dir") + "/conf.properties")
        } catch (e: FileNotFoundException) {
            log.severe("SERVER: CONF FILE: \"conf.properties\" missing in ${System.getProperty("jboss.server.config.dir")}")
        } catch (e: SecurityException) {
            log.severe("SERVER: CONF FILE: \"conf.properties\" in ${System.getProperty("jboss.server.config.dir")} permission denied")
        }

        try {
            try {
                prop.load(impStream)
            } catch (e: IllegalArgumentException) {
                log.severe("SERVER: CONF FILE: \"conf.properties\" in ${System.getProperty("jboss.server.config.dir")} ${e.printStackTrace()}")
            } finally {
                impStream?.close()
            }
        } catch (e: IOException) {
            log.severe(e.printStackTrace().toString())
        }
        log.info("SERVER: FILE \"conf.properties\" OK")
    }

    /**
     * metoda na nacitanie nastavenia
     */
    override fun getProp(key: String): String {
        return prop.getProperty(key)
    }

}
