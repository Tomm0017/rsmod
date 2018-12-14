package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.def.VarpDef
import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.SendChatboxTextMessage
import gg.rsmod.game.message.impl.SendSkillMessage
import gg.rsmod.game.message.impl.SetBigVarpMessage
import gg.rsmod.game.message.impl.SetSmallVarpMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.interf.Interfaces
import gg.rsmod.game.sync.UpdateBlock

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

    /**
     * The base region [Tile] is the most bottom-left (south-west) tile where
     * the region begins.
     */
    var lastKnownRegionBase: Tile? = null

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

    val interfaces: Interfaces by lazy { Interfaces(this) }

    val skills: SkillSet by lazy { SkillSet(maxSkills = world.gameContext.skillCount) }

    val varps: VarpSet by lazy { VarpSet(maxVarps = world.definitions.getCount(VarpDef::class.java)) }

    val localPlayers = arrayListOf<Player>()

    /**
     * An array that holds the last-known [Tile.to30BitInteger] for every player
     * according to this [Player]. This can vary from player to player, since
     * on log-in, this array will be filled with [0]s for this [Player].
     */
    val otherPlayerTiles = IntArray(2048)

    var runEnergy = 100.0

    override fun getType(): EntityType = EntityType.PLAYER

    override fun cycle() {
        if (pendingLogout) {
            if (lock.canLogout()) {
                // TODO: check if can log out (not in combat)
                handleLogout()
                return
            }
            pendingLogout = false
        }

        val newChunk = world.regions.getChunkForTile(tile)
        if (chunk == null || chunk != newChunk) {
            val oldRegion = lastTile?.toRegionId() ?: -1
            if (oldRegion != tile.toRegionId()) {
                world.plugins.executeRegionExit(this, oldRegion)
                world.plugins.executeRegionEnter(this, tile.toRegionId())
            }
            chunk = newChunk
        }

        for (i in 0 until skills.maxSkills) {
            if (skills.isDirty(i)) {
                write(SendSkillMessage(i, skills.getCurrentLevel(i), skills.getCurrentXp(i).toInt()))
            }
        }

        for (i in 0 until varps.maxVarps) {
            if (varps.isDirty(i)) {
                val varp = varps[i]
                val message = when {
                    varp.state <= Byte.MAX_VALUE -> SetSmallVarpMessage(varp.id, varp.state)
                    else -> SetBigVarpMessage(varp.id, varp.state)
                }
                write(message)
            }
        }

        skills.clean()
        varps.clean()
    }

    /**
     * Default method to check if a player is dead. We assume that the [Skill]
     * with id of [3] is Hitpoints.
     */
    override fun isDead(): Boolean = skills.getCurrentLevel(3) == 0

    /**
     * Checks if the player is running. We assume that the [Varp] with id of
     * [173] is the running state varp.
     */
    override fun isRunning(): Boolean = varps[173].state != 0

    /**
     * Handles the logic that must be executed once a player has successfully
     * logged out. This means all the prerequisites have been met for the player
     * to log out of the [world].
     *
     * The [Client] implementation overrides this method and will handle saving
     * data for the player and call this super method at the end.
     */
    protected open fun handleLogout() {
        world.unregister(this)
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
        blockBuffer.addBlock(UpdateBlock.APPEARANCE, getType())
        world.plugins.executeLogin(this)
    }

    /**
     * Checks if the player is registered to a [PawnList] as they should be
     * solely responsible for write access on the index. Being registered
     * to the list should essentially mean the player is registered to the
     * [world].
     *
     * @return [true] if the player is registered to a [PawnList].
     */
    fun isOnline(): Boolean = index > 0

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

    /**
     * Default method to close the attached channel. Won't be handled unless
     * the [Player] is controlled by a [Client] user.
     */
    open fun channelClose() {

    }

    fun message(message: String) {
        write(SendChatboxTextMessage(type = 0, message = message, username = null))
    }

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("name", username)
            .add("pid", index)
            .toString()
}