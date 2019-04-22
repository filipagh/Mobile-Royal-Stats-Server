package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable
import java.net.URL
import javax.json.bind.annotation.JsonbProperty


@JsonIgnoreProperties(ignoreUnknown = true)
open class UserStat: Serializable {
    var name: String? = null
    var tag: String? = null
    val trophies: String? = null
    @JsonbProperty("clan")
    val clan: Clan? = null
    @JsonbProperty("stats")
    val stats: Stats? = null
    @JsonbProperty("games")
    val games: Games? = null
    //TODO https://stackoverflow.com/questions/40862477/deserialize-complex-json-to-java-classes-nested-multiple-levels-deep

    @JsonIgnoreProperties(ignoreUnknown = true)
    inner class Clan : Serializable {
        val name: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    inner class Stats : Serializable {
        val level: String? = null
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    inner class Games : Serializable {
        val warDayWins: String? = null
        var winsPercent: String? = null
        var winsPercentLast10: String? = null
        var winsPercentLast20: String? = null
        var winsPercentLiveTime: String? = null
        var warDayBattleMiss: String? = "0"
    }

}


fun jsonToObject(json: String): UserStat {
    val mapper = ObjectMapper()
    var playerstat = mapper.readValue(json, UserStat::class.java)

    // parse data from html // LEBO TO NIEJE V API................
    val urlString = "https://royaleapi.com/inc/player/cw_history?player_tag=${playerstat.tag}"
    val url = URL(urlString)
    val conn = url.openConnection()
    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")

    // spracovanie http response do str
    var rider =  conn.getInputStream().bufferedReader()
    var result = StringBuffer()
    do {
        val line = rider.readLine()
        if(line != null) {
            result.append(line)
        }
    } while (line != null)

    // parsovanie winPercent
    val regex = Regex("<td>[0-9]+%</td>")
    val regexNum = Regex("[0-9]+%")
    val regexMissBattle = Regex("<div class=\"war_hit   missed\">")
    var last = 0
    for (i in 1..3) {
        val hit = regex.find(result,last)

        // parsovanie cisla
        val out=regexNum.find(hit!!.groups[0]!!.value)
        when (i) {
            1 -> playerstat.games!!.winsPercentLast10= out!!.value
            2 -> playerstat.games!!.winsPercentLast20= out!!.value
            3 -> playerstat.games!!.winsPercentLiveTime= out!!.value
        }
        last = hit.groups[0]!!.range.last
    }

    // parsovanie warDayBattleMiss
    var hit =  regexMissBattle.find(result)
    var missBattle = 0
    while (hit!=null) {
        missBattle +=1
        last = hit.groups[0]!!.range.last
        hit = regexMissBattle.find(result,last)
    }
    playerstat.games!!.warDayBattleMiss = missBattle.toString()


    //coretion data
    playerstat.games!!.winsPercent = (playerstat.games!!.winsPercent!!.toDouble()  * 100).toInt().toString()


    return playerstat
}