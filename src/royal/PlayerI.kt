package royal

import javax.ejb.Local

@Local
interface PlayerI {
    fun dajHraca(id : String) : String
}