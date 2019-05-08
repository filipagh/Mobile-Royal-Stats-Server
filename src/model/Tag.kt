package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable


/**
 * model pre tag
 */
@JsonIgnoreProperties(ignoreUnknown = true)
open class Tag: Serializable {
    var tag: String? = null
}
