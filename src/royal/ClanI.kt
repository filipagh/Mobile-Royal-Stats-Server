package royal

import javax.ejb.Local

@Local
interface ClanI {
    fun loadClanStats(id : String) : String
}