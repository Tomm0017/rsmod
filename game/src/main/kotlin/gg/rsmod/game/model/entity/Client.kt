package gg.rsmod.game.model.entity

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.service.serializer.PlayerSerializerService
import gg.rsmod.game.system.GameSystem
import gg.rsmod.net.codec.login.LoginRequest
import io.netty.channel.Channel

/**
 * A [Player] that is controlled by a human. A [Client] is responsible for
 * handling any network related job.
 *
 * Anything other than network logic should be added to [Player] instead.
 *
 * @param channel
 * The [Channel] used to write and read [Message]s to and from the client.
 *
 * @param world
 * The [World] that this client is registered to.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Client(val channel: Channel, world: World) : Player(world) {

    /**
     * The [System] that will handle [Message]s, write [Message]s and flush the
     * [Channel].
     */
    lateinit var gameSystem: GameSystem
    /**
     * The username that was used to register the [Player]. This username should
     * never be changed through the player's end.
     */
    lateinit var loginUsername: String

    /**
     * The encrypted password.
     */
    lateinit var passwordHash: String

    /**
     * The client's UUID.
     */
    lateinit var uuid: String

    /**
     * The xteas for the current log-in session.
     */
    lateinit var currentXteaKeys: IntArray

    /**
     * Is the applet focused on the player's computer?
     */
    var appletFocused = true

    /**
     * The applet's current width.
     */
    var clientWidth = 765

    /**
     * The applet's current height.
     */
    var clientHeight = 503

    var cameraPitch = 0

    var cameraYaw = 0

    override val entityType: EntityType = EntityType.CLIENT

    override fun handleLogout() {
        super.handleLogout()
        world.getService(PlayerSerializerService::class.java, searchSubclasses = true)?.saveClientData(this)
    }

    override fun handleMessages() {
        gameSystem.handleMessages()
    }

    override fun write(vararg messages: Message) {
        messages.forEach { m -> gameSystem.write(m) }
    }

    override fun write(vararg messages: Any) {
        messages.forEach { m -> channel.write(m) }
    }

    override fun channelFlush() {
        gameSystem.flush()
    }

    override fun channelClose() {
        gameSystem.close()
    }

    companion object {

        /**
         * Constructs a [Client] based on the [LoginRequest].
         */
        fun fromRequest(world: World, request: LoginRequest): Client {
            val client = Client(request.channel, world)
            client.clientWidth = request.clientWidth
            client.clientHeight = request.clientHeight
            client.loginUsername = request.username
            client.username = request.username
            client.uuid = request.uuid
            client.currentXteaKeys = request.xteaKeys
            return client
        }
    }
}
