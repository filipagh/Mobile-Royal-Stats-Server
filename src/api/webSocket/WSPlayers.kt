package api.webSocket

import com.fasterxml.jackson.databind.ObjectMapper
import model.Tag
import model.jsonToObject
import royal.PlayerI
import java.util.logging.Logger
import javax.ejb.EJB
import javax.ejb.Stateless
import javax.websocket.*
import javax.websocket.server.ServerEndpoint


@ServerEndpoint("/example")
@Stateless
open class WSPlayers {

    internal var log = Logger.getLogger(WSPlayers::class.java.name)

    @EJB
    private lateinit var players: PlayerI

    @OnMessage
    open fun receiveMessage(message: String, session: Session): String {
        val mapper = ObjectMapper()
        log.info("Received : " + message + ", session:" + session.getId())
        val tag = mapper.readValue(message, Tag::class.java)
        val player = jsonToObject(players.dajHraca(tag.tag!!))
        val json = mapper.writeValueAsString(player)
        return json
    }


    @OnOpen
    open fun open(session: Session) {

        log.info("Open session:" + session.getId())

    }


    @OnClose
    open fun close(session: Session, c: CloseReason) {
        log.info("Closing:" + session.getId())

    }

}