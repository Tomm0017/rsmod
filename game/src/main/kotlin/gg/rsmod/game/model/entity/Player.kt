package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.def.VarpDef
import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.*
import gg.rsmod.game.model.container.ContainerStackType
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.interf.Interfaces
import gg.rsmod.game.service.game.ItemStatsService
import gg.rsmod.game.sync.UpdateBlockType
import java.util.*

/**
 * A [Pawn] that represents a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
open class Player(override val world: World) : Pawn(world) {

    companion object {
        /**
         * How many tiles a player can see at a time.
         */
        const val VIEW_DISTANCE = 15
    }

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
     * the last known region for this player begins.
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
     * For example: when the player is in combat.
     */
    @Volatile private var pendingLogout = false

    val inventory by lazy { ItemContainer(world.definitions, 28, ContainerStackType.NORMAL) }

    val equipment by lazy { ItemContainer(world.definitions, 14, ContainerStackType.NORMAL) }

    val interfaces by lazy { Interfaces(this) }

    private val normalSkills by lazy { SkillSet(maxSkills = world.gameContext.skillCount) }

    val varps  by lazy { VarpSet(maxVarps = world.definitions.getCount(VarpDef::class.java)) }

    val localPlayers = arrayListOf<Player>()

    val otherPlayerSkipFlags = IntArray(2048)

    /**
     * An array that holds the last-known [Tile.to30BitInteger] for every player
     * according to this [Player]. This can vary from player to player, since
     * on log-in, this array will be filled with [0]s for this [Player].
     */
    val otherPlayerTiles = IntArray(2048)

    /**
     * A flag that represents whether or not we want to remove our
     * [Interfaces.currentMainScreenInterface] from our [Interfaces.visible] map
     * near the end of the next available game cycle.
     *
     * It can't be removed immediately due to the [CloseMainInterfaceMessage]
     * being received before [ClickButtonMessage], which leads to the server
     * thinking that the player is trying to click a button on an interface
     * that's not in their [Interfaces.visible] map.
     */
    var closeMainInterface = false

    /**
     * Persistent attributes which must be saved from our system and loaded
     * when needed. This map does not support storing [Double]s as we convert
     * every double into an [Int] when loading. This is done because some
     * parsers can interpret [Number]s differently, so we want to force every
     * [Number] to an [Int], explicitly. If you wish to store a [Double], you
     * can multiply your value by [100] and then divide it on login as a work-
     * around.
     */
    private val persistentAttr: MutableMap<String, Any> = hashMapOf()

    val looks = intArrayOf(9, 14, 109, 26, 33, 36, 42)

    val lookColors = intArrayOf(0, 3, 2, 0, 0)

    var weight = 0.0

    var gender = Gender.MALE

    var skullIcon = -1

    var runEnergy = 100.0

    override fun getType(): EntityType = EntityType.PLAYER

    /**
     * Logic that should be executed every game cycle, before
     * [gg.rsmod.game.sync.task.PlayerSynchronizationTask].
     *
     * Note that this method may be handled in parallel, so be careful with race
     * conditions if any logic may modify other [Pawn]s.
     */
    override fun cycle() {
        var calculateWeight = false

        if (pendingLogout) {
            if (lock.canLogout()) {
                // TODO: check if can log out (not in combat)
                handleLogout()
                return
            }
            pendingLogout = false
        }

        val oldRegion = lastTile?.toRegionId() ?: -1
        if (oldRegion != tile.toRegionId()) {
            if (oldRegion != -1) {
                world.plugins.executeRegionExit(this, oldRegion)
            }
            world.plugins.executeRegionEnter(this, tile.toRegionId())
        }

        if (inventory.dirty) {
            // NOTE(Tom): can add a plugin that executes when an item container
            // is dirty since these values can change per revision
            write(SetItemContainerMessage(parent = 149, child = 0, containerKey = 93, items = Arrays.copyOf(inventory.getBackingArray(), inventory.capacity)))
            inventory.dirty = false
            calculateWeight = true
        }

        if (equipment.dirty) {
            write(SetItemContainerMessage(containerKey = 94, items = Arrays.copyOf(equipment.getBackingArray(), equipment.capacity)))
            equipment.dirty = false
            calculateWeight = true
        }

        if (calculateWeight) {
            world.getService(ItemStatsService::class.java, searchSubclasses = false).ifPresent { s ->
                val inventoryWeight = inventory.filterNotNull().sumByDouble { s.get(it.id)?.weight ?: 0.0 }
                val equipmentWeight = equipment.filterNotNull().sumByDouble { s.get(it.id)?.weight ?: 0.0 }
                weight = inventoryWeight + equipmentWeight
            }
        }

        for (i in 0 until getSkills().maxSkills) {
            if (getSkills().isDirty(i)) {
                write(SendSkillMessage(skill = i, level = getSkills().getCurrentLevel(i), xp = getSkills().getCurrentXp(i).toInt()))
            }
        }
        getSkills().clean()

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
        varps.clean()

        val timerIterator = timers.getTimers().entries.iterator()
        while (timerIterator.hasNext()) {
            val timer = timerIterator.next()

            if (timer.value <= 0) {
                // NOTE(Tom): if any timer may modify another [Pawn], we will
                // need to iterate timers on a sequential task and execute
                // any of them which have a value (time) of [0], instead of
                // handling it here. This would only apply if we are using
                // a parallel task to call [cycle].
                world.plugins.executeTimer(this, timer.key)
                if (!timers.has(timer.key)) {
                    timerIterator.remove()
                }
            }
        }

        timers.getTimers().entries.forEach { timer ->
            timer.setValue(timer.value - 1)
        }
    }

    /**
     * Logic that should be executed every game cycle, after
     * [gg.rsmod.game.sync.task.PlayerSynchronizationTask].
     *
     * Note that this method may be handled in parallel, so be careful with race
     * conditions if any logic may modify other [Pawn]s.
     */
    fun postCycle() {
        /**
         * Close the main interface if it's pending.
         */
        if (closeMainInterface) {
            interfaces.closeMain()
            closeMainInterface = false
        }

        /**
         * Flush the channel at the end.
         */
        channelFlush()
    }

    /**
     * Default method to check if a player is dead. We assume that the [Skill]
     * with id of [3] is Hitpoints.
     */
    override fun isDead(): Boolean = getSkills().getCurrentLevel(3) == 0

    /**
     * Checks if the player is running. We assume that the [Varp] with id of
     * [173] is the running state varp.
     */
    override fun isRunning(): Boolean = varps[173].state != 0

    override fun addBlock(block: UpdateBlockType) {
        val bits = world.playerUpdateBlocks.updateBlocks[block]!!
        blockBuffer.addBit(bits.bit)
    }

    override fun hasBlock(block: UpdateBlockType): Boolean {
        val bits = world.playerUpdateBlocks.updateBlocks[block]!!
        return blockBuffer.hasBit(bits.bit)
    }

    override fun heal(amount: Int, capValue: Int) {
        getSkills().alterCurrentLevel(skill = 3, value = amount, capValue = capValue)
    }

    fun getSkills(): SkillSet = normalSkills

    /**
     * Handles the logic that must be executed once a player has successfully
     * logged out. This means all the prerequisites have been met for the player
     * to log out of the [world].
     *
     * The [Client] implementation overrides this method and will handle saving
     * data for the player and call this super method at the end.
     */
    protected open fun handleLogout() {
        interruptPlugins()
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
        if (getType().isHumanControlled()) {
            localPlayers.add(this)
            write(LoginRegionMessage(index, tile, world.xteaKeyService))
        }

        initiated = true
        addBlock(UpdateBlockType.APPEARANCE)
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

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistentAttr(key: String): T = (persistentAttr[key] as? T)!!

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistentNullableAttr(key: String): T? = (persistentAttr[key] as? T)

    @Suppress("UNCHECKED_CAST")
    fun <T> getPersistentOrDefaultAttr(key: String, default: T): T = (persistentAttr[key] as? T) ?: default

    @Suppress("UNCHECKED_CAST")
    fun <T> putPersistentAttr(key: String, value: T) {
        persistentAttr[key] = value as Any
    }

    fun removePersistentAttr(key: String) {
        persistentAttr.remove(key)
    }

    /**
     * Should only be used when saving [persistentAttr] attributes.
     */
    fun __getPersistentAttrMap(): Map<String, Any> = persistentAttr

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("name", username)
            .add("pid", index)
            .toString()
}