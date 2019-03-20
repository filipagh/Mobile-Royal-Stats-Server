package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable
import javax.json.bind.annotation.JsonbProperty



@JsonIgnoreProperties(ignoreUnknown = true)
open class UserStat: Serializable {
    var name: String? = null
    var tag: String? = null
    @JsonbProperty("clan")
    val clan: ClanTest? = null


//    @JsonbProperty("clan")
//    var clan: String? = null


    //TODO https://stackoverflow.com/questions/40862477/deserialize-complex-json-to-java-classes-nested-multiple-levels-deep
}

@JsonIgnoreProperties(ignoreUnknown = true)
open class ClanTest: Serializable{
     val name: String? = null
 }




fun aa(json: String): UserStat {
    val mapper = ObjectMapper()
    return mapper.readValue(json, UserStat::class.java)

}