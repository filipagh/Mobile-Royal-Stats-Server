package api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import model.Tag
import model.UserStat
import model.jsonToObject
import royal.PlayerI
import javax.ejb.EJB
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType


//@Secured
@Api(value = "Players")
@Path("/Players")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
open class Players {

    @EJB
    private lateinit var players: PlayerI

    @ApiOperation(value = "Get Player Stats", notes = "ID is ignored (should be removed)")
    @POST
    @Path("/info")
    open fun info(tag : Tag): UserStat {
        val player = jsonToObject(players.dajHraca(tag.tag!!))
        return player
    }
}



