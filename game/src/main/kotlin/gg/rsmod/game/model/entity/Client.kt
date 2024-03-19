package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.message.Message
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.World
import gg.rsmod.game.service.serializer.PlayerSerializerService
import gg.rsmod.game.system.GameSystem
import gg.rsmod.net.codec.login.LoginRequest
import gg.rsmod.net.packet.GamePacket
import io.netty.channel.Channel
import gg.rsmod.game.message.impl.*

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

    /**
     * The pitch of the camera in the client's game UI.
     */
    var cameraPitch = 0

    /**
     * The yaw of the camera in the client's game UI.
     */
    var cameraYaw = 0

    /**
     * A flag which indicates that the client will have their incoming packets
     * ([gg.rsmod.game.message.Message]s) logged.
     */
    var logPackets = true

    override val entityType: EntityType = EntityType.CLIENT

    override fun handleLogout() {
        super.handleLogout()
        world.getService(PlayerSerializerService::class.java, searchSubclasses = true)?.saveClientData(this)
    }

    override fun handleMessages() {
        gameSystem.handleMessages()
    }

    private val completedMessages = listOf(VarpSmallMessage::class, VarpLargeMessage::class, UpdateStatMessage::class,
        UpdateRunEnergyMessage::class, UpdateRunWeightMessage::class, RunClientScriptMessage::class,
        IfSetEventsMessage::class, IfOpenSubMessage::class, IfSetTextMessage::class,
        UpdateZonePartialEnclosedMessage::class, RebuildNormalMessage::class, RebuildLoginMessage::class,
        IfOpenTopMessage::class, MidiSongMessage::class, MidiJingleMessage::class, UpdateInvFullMessage::class,
        MessageGameMessage::class, SetOpPlayerMessage::class, IfMoveSubMessage::class, LogoutFullMessage::class)

    private var rebuildNormalMessageWritten = false
    private val pendingMessages = mutableListOf<Message>()
    override fun write(vararg messages: Message) {
        messages.forEach { message ->
            if (!rebuildNormalMessageWritten && message is RebuildNormalMessage) {
                gameSystem.write(message)
                rebuildNormalMessageWritten = true
                onRebuildNormalMessageWritten()
            } else if (rebuildNormalMessageWritten || message is RebuildLoginMessage) {
                gameSystem.write(message)
            } else {
                println(message)
                pendingMessages.add(message)
            }
        }
    }

    private fun onRebuildNormalMessageWritten() {
        pendingMessages.forEach { message ->
            gameSystem.write(message)
        }
        pendingMessages.clear()
    }

    override fun write(vararg messages: Any) {
        messages.forEach {
            if (it is GamePacket && it.opcode == 23) {
                channel.write(it)
            } else {
                println("Unhandled Server Packet: $it")
            }
        }
    }

    override fun channelFlush() {
        gameSystem.flush()
    }

    override fun channelClose() {
        gameSystem.close()
    }

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("login_username", loginUsername)
            .add("username", username)
            .add("channel", channel)
            .toString()

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
