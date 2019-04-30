package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable
import java.util.logging.Logger

/**
 * model clanStats z RoyalApi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class ClanStats: Serializable {
    var name: String? = null
    var tag: String? = null
    val memberCount: String? = null
    @JsonProperty("members")
    private val players: List<Player>? = emptyList()

    @JsonIgnoreProperties(ignoreUnknown = true)
     class Player : Serializable {
        var name: String? = null
        var tag: String? = null
        var role: String? = null
        var trophies: String? = null
        var donations: String? = null
        var expLevel: String? = null
    }
}

/**
 * metoda na nacitanie ClanStats z jsonu
 * @param json ClanStats
 * @return clanStats
 */
fun jsonToClanStats(json: String): ClanStats{
    val log = Logger.getLogger("JsonMapper")

    val mapper = ObjectMapper()
    var clanStats = mapper.readValue(json, ClanStats::class.java)

    log.info("LOAD clanStats royalApi endpoint with clan tag:${clanStats.tag}")
    return clanStats
}