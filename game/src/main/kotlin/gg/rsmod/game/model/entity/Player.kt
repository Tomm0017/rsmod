package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.map.Chunk
import gg.rsmod.game.map.Region
import gg.rsmod.game.model.PlayerGpi
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.interf.Interfaces
import gg.rsmod.game.model.interf.action.OSRSInterfaceListener
import gg.rsmod.game.message.Message

/**
 * A [Pawn] that represents a player on the players' clients.
 *
 * @author Tom <rspsmods@gmail.com>
 */
open class Player(override val world: World) : Pawn(world) {

    /**
     * A persistent and unique id. This is <strong>not</strong> the index
     * of our [Player] when registered to the [World], it is a value determined
     * when the [Player] first registers their account.
     */
    var id = -1

    var username = ""

    var privilege = Privilege.DEFAULT

    var mapChunk: Chunk? = null

    var mapRegion: Region? = null

    /**
     * The base region [Tile] is the most bottom-left (south-west) tile where
     * the region begins.
     */
    var lastKnownRegionBase: Tile? = null

    val localPlayers = arrayListOf<Player>()

    var teleport = true

    /**
     * A flag that indicates whether or not the [login] method has been executed.
     * This is currently used so that we don't send player updates when the player
     * hasn't been fully initialized. We can test later to see if this is even
     * necessary.
     */
    var initiated = false

    /**
     * A flag which indicates the player is attempting to log out. There can be
     * certain circumstances where the player should not be unregistered from
     * the world.
     *
     * For example: when they player is in combat.
     */
    @Volatile private var pendingLogout = false

    val interfaces = lazy { Interfaces(this, OSRSInterfaceListener()) }.value

    override fun getType(): EntityType = EntityType.PLAYER

    override fun cycle() {
        if (pendingLogout) {
            if (lock.canLogout()) {
                // TODO: check if can log out (not in combat)
                world.unregister(this)
            }
            pendingLogout = false
        }
    }

    /**
     * Requests for this player to log out. However, the player may not be able
     * to log out immediately under certain circumstances.
     */
    fun requestLogout() {
        pendingLogout = true
    }

    /**
     * Registers this player to the [world].
     */
    fun register(): Boolean {
        return world.register(this)
    }

    /**
     * Handles any logic that should be executed upon log in.
     */
    fun login() {
        if (this is Client) {
            PlayerGpi.init(this)
        }
        initiated = true
        localPlayers.add(this)
        world.server.getPlugins().executeLogin(this)
    }

    /**
     * Checks if the player is registered to a [PawnList] as they should be
     * solely responsible for write access on the index. Being registered
     * to the list should essentially mean the player is registered to the
     * [world].
     *
     * @return [true] if the player is registered to a [PawnList].
     */
    fun isOnline(): Boolean {
        return index > 0
    }

    /**
     * Default method to handle any incoming [Message]s that won't be
     * handled unless the [Player] is controlled by a [Client] user.
     */
    open fun handleMessages() {

    }

    /**
     * Default method to write [Message]s to the attached channel that won't
     * be handled unless the [Player] is controlled by a [Client] user.
     */
    open fun write(vararg messages: Message) {

    }

    open fun write(vararg messages: Any) {

    }

    /**
     * Default method to flush the attached channel. Won't be handled unless
     * the [Player] is controlled by a [Client] user.
     */
    open fun channelFlush() {

    }

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("name", username)
            .add("pid", index)
            .toString()
}