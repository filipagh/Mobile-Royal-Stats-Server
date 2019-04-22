package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

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

fun jsonToClanStats(json: String): ClanStats{
    val mapper = ObjectMapper()
    var clanStats = mapper.readValue(json, ClanStats::class.java)
    return clanStats
}