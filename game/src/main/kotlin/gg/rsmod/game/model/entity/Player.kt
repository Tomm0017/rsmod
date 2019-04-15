package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.def.VarpDef
import gg.rsmod.game.message.Message
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.*
import gg.rsmod.game.model.attr.CURRENT_SHOP_ATTR
import gg.rsmod.game.model.attr.LEVEL_UP_INCREMENT
import gg.rsmod.game.model.attr.LEVEL_UP_OLD_XP
import gg.rsmod.game.model.attr.LEVEL_UP_SKILL_ID
import gg.rsmod.game.model.container.ItemContainer
import gg.rsmod.game.model.container.key.BANK_KEY
import gg.rsmod.game.model.container.key.ContainerKey
import gg.rsmod.game.model.container.key.EQUIPMENT_KEY
import gg.rsmod.game.model.container.key.INVENTORY_KEY
import gg.rsmod.game.model.interf.InterfaceSet
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.game.model.skill.SkillSet
import gg.rsmod.game.model.timer.ACTIVE_COMBAT_TIMER
import gg.rsmod.game.model.timer.FORCE_DISCONNECTION_TIMER
import gg.rsmod.game.model.varp.VarpSet
import gg.rsmod.game.sync.block.UpdateBlockType
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*
import kotlin.collections.HashMap

/**
 * A [Pawn] that represents a player.
 *
 * @author Tom <rspsmods@gmail.com>
 */
open class Player(world: World) : Pawn(world) {

    /**
     * A persistent and unique id. This is <strong>not</strong> the index
     * of our [Player] when registered to the [World], it is a value determined
     * when the [Player] first registers their account.
     */
    lateinit var uid: PlayerUID

    /**
     * The display name that will show on the player while in-game.
     */
    var username = ""

    /**
     * @see Privilege
     */
    var privilege = Privilege.DEFAULT

    /**
     * The base region [Coordinate] is the most bottom-left (south-west) tile where
     * the last known region for this player begins.
     */
    var lastKnownRegionBase: Coordinate? = null

    /**
     * A flag that indicates whether or not the [login] method has been executed.
     * This is currently used so that we don't send player updates when the player
     * hasn't been fully initialized. We can test later to see if this is even
     * necessary.
     */
    var initiated = false

    /**
     * The index that was assigned to a [Player] when they are first registered to the
     * [World]. This is needed to remove local players from the synchronization task
     * as once that logic is reached, the local player would have an index of [-1].
     */
    var lastIndex = -1

    /**
     * A flag which indicates the player is attempting to log out. There can be
     * certain circumstances where the player should not be unregistered from
     * the world.
     *
     * For example: when the player is in combat.
     */
    @Volatile private var pendingLogout = false

    /**
     * A flag which indicates that our [FORCE_DISCONNECTION_TIMER] must be set
     * when [pendingLogout] logic is handled.
     */
    @Volatile private var setDisconnectionTimer = false

    val inventory = ItemContainer(world.definitions, INVENTORY_KEY)

    val equipment = ItemContainer(world.definitions, EQUIPMENT_KEY)

    val bank = ItemContainer(world.definitions, BANK_KEY)

    /**
     * A map that contains all the [ItemContainer]s a player can have.
     */
    val containers = HashMap<ContainerKey, ItemContainer>().apply {
        put(INVENTORY_KEY,  inventory)
        put(EQUIPMENT_KEY,  equipment)
        put(BANK_KEY,       bank)
    }

    val interfaces by lazy { InterfaceSet(this) }

    val varps = VarpSet(maxVarps = world.definitions.getCount(VarpDef::class.java))

    private val skillSet = SkillSet(maxSkills = world.gameContext.skillCount)

    /**
     * The options that can be executed on this player
     */
    val options = Array<String?>(10) { null }

    /**
     * Flag that indicates whether or not to refresh the shop the player currently
     * has open.
     */
    var shopDirty = false

    /**
     * Some areas have a 'large' viewport. Which means the player's client is
     * able to render more entities in a larger radius than normal.
     */
    private var largeViewport = false

    /**
     * The players in our viewport, including ourselves. This list should not
     * be used outside of our synchronization task.
     */
    internal val gpiLocalPlayers = arrayOfNulls<Player>(2048)

    /**
     * The indices of any possible local player in the world.
     */
    internal val gpiLocalIndexes = IntArray(2048)

    /**
     * The current local player count.
     */
    internal var gpiLocalCount = 0

    /**
     * The indices of players outside of our viewport in the world.
     */
    internal val gpiExternalIndexes = IntArray(2048)

    /**
     * The amount of players outside of our viewport.
     */
    internal var gpiExternalCount = 0

    /**
     * The inactivity flags for players.
     */
    internal val gpiInactivityFlags = IntArray(2048)

    /**
     * GPI tile hash multipliers.
     *
     * The player synchronization task will send [Tile.x] and [Tile.z] as 13-bit
     * values, which is 2^13 (8192). To send a player position higher than said
     * value in either direction, we must also send a multiplier.
     */
    internal val gpiTileHashMultipliers = IntArray(2048)

    /**
     * The npcs in our viewport. This list should not be used outside of our
     * synchronization task.
     */
    internal val localNpcs = ObjectArrayList<Npc>()

    var appearance = Appearance.DEFAULT

    var weight = 0.0

    var skullIcon = -1

    var runEnergy = 100.0

    /**
     * The current combat level. This must be set externally by a login plugin
     * that is used on whatever revision you want.
     */
    var combatLevel = 3

    var gameMode = 0

    var xpRate = 1.0

    fun getSkills(): SkillSet = skillSet

    override fun getType(): EntityType = EntityType.PLAYER

    /**
     * Checks if the player is running. We assume that the varp with id of
     * [173] is the running state varp.
     */
    override fun isRunning(): Boolean = varps[173].state != 0 || movementQueue.peekLastStep()?.type == MovementQueue.StepType.FORCED_RUN

    override fun getSize(): Int = 1

    override fun getCurrentHp(): Int = getSkills().getCurrentLevel(3)

    override fun getMaxHp(): Int = getSkills().getMaxLevel(3)

    override fun setCurrentHp(level: Int) {
        getSkills().setCurrentLevel(3, level)
    }

    override fun addBlock(block: UpdateBlockType) {
        val bits = world.playerUpdateBlocks.updateBlocks[block]!!
        blockBuffer.addBit(bits.bit)
    }

    override fun hasBlock(block: UpdateBlockType): Boolean {
        val bits = world.playerUpdateBlocks.updateBlocks[block]!!
        return blockBuffer.hasBit(bits.bit)
    }

    /**
     * Logic that should be executed every game cycle, before
     * [gg.rsmod.game.sync.task.PlayerSynchronizationTask].
     *
     * Note that this method may be handled in parallel, so be careful with race
     * conditions if any logic may modify other [Pawn]s.
     */
    override fun cycle() {
        var calculateWeight = false
        var calculateBonuses = false

        if (pendingLogout) {

            /*
             * If a channel is suddenly inactive (disconnected), we don't to
             * immediately unregister the player. However, we do want to
             * unregister the player abruptly if a certain amount of time
             * passes since their channel disconnected.
             */
            if (setDisconnectionTimer) {
                timers[FORCE_DISCONNECTION_TIMER] = 250 // 2 mins 30 secs
                setDisconnectionTimer = false
            }

            /*
             * A player should only be unregistered from the world when they
             * do not have [ACTIVE_COMBAT_TIMER] or its cycles are <= 0, or if
             * their channel has been inactive for a while.
             *
             * We do allow players to disconnect even if they are in combat, but
             * only if the most recent damage dealt to them are by npcs.
             */
            val stopLogout = timers.has(ACTIVE_COMBAT_TIMER) && damageMap.getAll(type = EntityType.PLAYER, timeFrameMs = 10_000).isNotEmpty()
            val forceLogout = timers.exists(FORCE_DISCONNECTION_TIMER) && !timers.has(FORCE_DISCONNECTION_TIMER)

            if (!stopLogout || forceLogout) {
                if (lock.canLogout()) {
                    handleLogout()
                    return
                }
            }
        }

        val oldRegion = lastTile?.regionId ?: -1
        if (oldRegion != tile.regionId) {
            if (oldRegion != -1) {
                world.plugins.executeRegionExit(this, oldRegion)
            }
            world.plugins.executeRegionEnter(this, tile.regionId)
        }

        if (inventory.dirty) {
            write(UpdateInvFullMessage(parent = 149, child = 0, containerKey = 93, items = inventory.rawItems))
            inventory.dirty = false
            calculateWeight = true
        }

        if (equipment.dirty) {
            write(UpdateInvFullMessage(containerKey = 94, items = equipment.rawItems))
            equipment.dirty = false
            calculateWeight = true
            calculateBonuses = true

            addBlock(UpdateBlockType.APPEARANCE)
        }

        if (bank.dirty) {
            write(UpdateInvFullMessage(containerKey = 95, items = bank.rawItems))
            bank.dirty = false
        }

        if (shopDirty) {
            attr[CURRENT_SHOP_ATTR]?.let { shop ->
                write(UpdateInvFullMessage(containerKey = 13, items = shop.items.map { if (it != null) Item(it.item, it.currentAmount) else null }.toTypedArray()))
            }
            shopDirty = false
        }

        if (calculateWeight || calculateBonuses) {
            calculateWeightAndBonus(weight = calculateWeight, bonuses = calculateBonuses)
        }

        if (timers.isNotEmpty) {
            timerCycle()
        }

        hitsCycle()

        for (i in 0 until varps.maxVarps) {
            if (varps.isDirty(i)) {
                val varp = varps[i]
                val message = when {
                    varp.state in -Byte.MAX_VALUE..Byte.MAX_VALUE -> VarpSmallMessage(varp.id, varp.state)
                    else -> VarpLargeMessage(varp.id, varp.state)
                }
                write(message)
            }
        }
        varps.clean()

        for (i in 0 until getSkills().maxSkills) {
            if (getSkills().isDirty(i)) {
                write(UpdateStatMessage(skill = i, level = getSkills().getCurrentLevel(i), xp = getSkills().getCurrentXp(i).toInt()))
                getSkills().clean(i)
            }
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
        /*
         * Flush the channel at the end.
         */
        channelFlush()
    }

    /**
     * Registers this player to the [world].
     */
    fun register(): Boolean = world.register(this)

    /**
     * Handles any logic that should be executed upon log in.
     */
    fun login() {
        if (getType().isHumanControlled()) {
            gpiLocalPlayers[index] = this
            gpiLocalIndexes[gpiLocalCount++] = index

            for (i in 1 until 2048) {
                if (i == index) {
                    continue
                }
                gpiExternalIndexes[gpiExternalCount++] = i
                gpiTileHashMultipliers[i] = if (i < world.players.capacity) world.players[i]?.tile?.asTileHashMultiplier ?: 0 else 0
            }

            val tiles = IntArray(gpiTileHashMultipliers.size)
            System.arraycopy(gpiTileHashMultipliers, 0, tiles, 0, tiles.size)

            write(RebuildLoginMessage(index, tile, tiles, world.xteaKeyService))
        }

        if (world.rebootTimer != -1) {
            write(UpdateRebootTimerMessage(world.rebootTimer))
        }

        initiated = true
        world.plugins.executeLogin(this)
    }

    /**
     * Requests for this player to log out. However, the player may not be able
     * to log out immediately under certain circumstances.
     */
    fun requestLogout() {
        pendingLogout = true
        setDisconnectionTimer = true
    }

    /**
     * Handles the logic that must be executed once a player has successfully
     * logged out. This means all the prerequisites have been met for the player
     * to log out of the [world].
     *
     * The [Client] implementation overrides this method and will handle saving
     * data for the player and call this super method at the end.
     */
    internal open fun handleLogout() {
        interruptQueues()
        world.instanceAllocator.logout(this)
        world.plugins.executeLogout(this)
        world.unregister(this)
    }

    /**
     * Calculate the current weight and equipment bonuses for the player.
     */
    fun calculateWeightAndBonus(weight: Boolean, bonuses: Boolean = true) {
        if (weight) {
            val inventoryWeight = inventory.filterNotNull().sumByDouble { it.getDef(world.definitions).weight }
            val equipmentWeight = equipment.filterNotNull().sumByDouble { it.getDef(world.definitions).weight }
            this.weight = inventoryWeight + equipmentWeight
            write(UpdateRunWeightMessage(this.weight.toInt()))
        }

        if (bonuses) {
            Arrays.fill(equipmentBonuses, 0)
            for (i in 0 until equipment.capacity) {
                val item = equipment[i] ?: continue
                val def = item.getDef(world.definitions)
                def.bonuses.forEachIndexed { index, bonus -> equipmentBonuses[index] += bonus }
            }
        }
    }

    fun addXp(skill: Int, xp: Double) {
        val oldXp = getSkills().getCurrentXp(skill)
        if (oldXp >= SkillSet.MAX_XP) {
            return
        }
        val newXp = Math.min(SkillSet.MAX_XP.toDouble(), (oldXp + (xp * xpRate)))
        /*
         * Amount of levels that have increased with the addition of [xp].
         */
        val increment = SkillSet.getLevelForXp(newXp) - SkillSet.getLevelForXp(oldXp)

        /*
         * Only increment the 'current' level if it's set at its capped level.
         */
        if (getSkills().getCurrentLevel(skill) == getSkills().getMaxLevel(skill)) {
            getSkills().setBaseXp(skill, newXp)
        } else {
            getSkills().setXp(skill, newXp)
        }

        if (increment > 0) {
            attr[LEVEL_UP_SKILL_ID] = skill
            attr[LEVEL_UP_INCREMENT] = increment
            attr[LEVEL_UP_OLD_XP] = oldXp
            world.plugins.executeSkillLevelUp(this)
        }
    }

    /**
     * @see largeViewport
     */
    fun setLargeViewport(largeViewport: Boolean) {
        this.largeViewport = largeViewport
    }

    /**
     * @see largeViewport
     */
    fun hasLargeViewport(): Boolean = largeViewport

    /**
     * Invoked when the player should close their current interface modal.
     */
    internal fun closeInterfaceModal() {
        world.plugins.executeModalClose(this)
    }

    /**
     * Checks if the player is registered to a [PawnList] as they should be
     * solely responsible for write access on the index. Being registered
     * to the list should essentially mean the player is registered to the
     * [world].
     *
     * @return
     * true if the player is registered to a [PawnList].
     */
    val isOnline: Boolean get() = index > 0

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

    /**
     * Write a [MessageGameMessage] to the client.
     */
    fun message(message: String) {
        write(MessageGameMessage(type = 0, message = message, username = null))
    }

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("name", username)
            .add("pid", index)
            .toString()

    companion object {
        /**
         * How many tiles a player can 'see' at a time, normally.
         */
        const val NORMAL_VIEW_DISTANCE = 15

        /**
         * How many tiles a player can 'see' at a time when in a 'large' viewport.
         */
        const val LARGE_VIEW_DISTANCE = 127

        /**
         * How many tiles in each direction a player can see at a given time.
         * This should be as far as players can see entities such as ground items
         * and objects.
         */
        const val TILE_VIEW_DISTANCE = 32
    }
}
