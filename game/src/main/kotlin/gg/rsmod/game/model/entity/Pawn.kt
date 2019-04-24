package gg.rsmod.game.model.entity

import gg.rsmod.game.action.NpcDeathAction
import gg.rsmod.game.action.PlayerDeathAction
import gg.rsmod.game.event.Event
import gg.rsmod.game.message.impl.SetMapFlagMessage
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.Graphic
import gg.rsmod.game.model.Hit
import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.PawnList
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeMap
import gg.rsmod.game.model.attr.COMBAT_TARGET_FOCUS_ATTR
import gg.rsmod.game.model.attr.FACING_PAWN_ATTR
import gg.rsmod.game.model.attr.INTERACTING_NPC_ATTR
import gg.rsmod.game.model.attr.INTERACTING_PLAYER_ATTR
import gg.rsmod.game.model.bits.INFINITE_VARS_STORAGE
import gg.rsmod.game.model.bits.InfiniteVarsType
import gg.rsmod.game.model.collision.CollisionManager
import gg.rsmod.game.model.combat.DamageMap
import gg.rsmod.game.model.path.FutureRoute
import gg.rsmod.game.model.path.PathFindingStrategy
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import gg.rsmod.game.model.path.strategy.BFSPathFindingStrategy
import gg.rsmod.game.model.path.strategy.SimplePathFindingStrategy
import gg.rsmod.game.model.queue.QueueTask
import gg.rsmod.game.model.queue.QueueTaskSet
import gg.rsmod.game.model.queue.TaskPriority
import gg.rsmod.game.model.queue.impl.PawnQueueTaskSet
import gg.rsmod.game.model.region.Chunk
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.RESET_PAWN_FACING_TIMER
import gg.rsmod.game.model.timer.STUN_TIMER
import gg.rsmod.game.model.timer.TimerMap
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.service.log.LoggerService
import gg.rsmod.game.sync.block.UpdateBlockBuffer
import gg.rsmod.game.sync.block.UpdateBlockType
import kotlinx.coroutines.CoroutineScope
import java.lang.ref.WeakReference
import java.util.ArrayDeque
import java.util.Queue

/**
 * A controllable character in the world that is used by something, or someone,
 * for their own purpose.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Pawn(val world: World) : Entity() {

    /**
     * The index assigned when this [Pawn] is successfully added to a [PawnList].
     */
    var index = -1

    /**
     * @see UpdateBlockBuffer
     */
    internal var blockBuffer = UpdateBlockBuffer()

    /**
     * The 3D [Tile] that this pawn was standing on, in the last game cycle.
     */
    internal var lastTile: Tile? = null

    /**
     * The last tile that was set for the pawn's [gg.rsmod.game.model.region.Chunk].
     */
    internal var lastChunkTile: Tile? = null

    /**
     * Whether or not this pawn can teleported this game cycle.
     */
    internal var teleport = false

    /**
     * @see [MovementQueue]
     */
    internal val movementQueue by lazy { MovementQueue(this) }

    /**
     * The current directions that this pawn is moving.
     */
    internal var steps: MovementQueue.StepDirection? = null

    /**
     * The last [Direction] this pawn was facing.
     */
    internal var lastFacingDirection: Direction = Direction.SOUTH

    /**
     * The current [LockState] which filters what actions this pawn can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the pawn.
     *
     * @see AttributeMap
     */
    val attr = AttributeMap()

    /**
     * The timers attached to the pawn.
     *
     * @see TimerMap
     */
    val timers = TimerMap()

    internal val queues: QueueTaskSet = PawnQueueTaskSet()

    /**
     * The equipment bonus for the pawn.
     */
    val equipmentBonuses = IntArray(14)

    /**
     * The current prayer icon that the pawn has active.
     */
    var prayerIcon = -1

    /**
     * Transmog is the action of turning into an npc. This value is equal to the
     * npc id of the npc you want to turn into, visually.
     */
    private var transmogId = -1

    /**
     * A list of pending [Hit]s.
     */
    private val pendingHits = mutableListOf<Hit>()

    /**
     * A [DamageMap] to keep track of who has dealt damage to this pawn.
     */
    val damageMap = DamageMap()

    /**
     * A flag which indicates if this pawn is visible to players in the world.
     */
    var invisible = false

    /**
     * The [FutureRoute] for the pawn, if any.
     * @see createPathFindingStrategy
     */
    private var futureRoute: FutureRoute? = null

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    fun isDead(): Boolean = getCurrentHp() == 0

    fun isAlive(): Boolean = !isDead()

    abstract fun isRunning(): Boolean

    abstract fun getSize(): Int

    abstract fun getCurrentHp(): Int

    abstract fun getMaxHp(): Int

    abstract fun setCurrentHp(level: Int)

    abstract fun addBlock(block: UpdateBlockType)

    abstract fun hasBlock(block: UpdateBlockType): Boolean

    /**
     * Lock the pawn to the default [LockState.FULL] state.
     */
    fun lock() {
        lock = LockState.FULL
    }

    /**
     * Unlock the pawn and set it to [LockState.NONE] state.
     */
    fun unlock() {
        lock = LockState.NONE
    }

    /**
     * Checks if the pawn has any lock state set.
     */
    fun isLocked(): Boolean = lock != LockState.NONE

    fun getTransmogId(): Int = transmogId

    fun setTransmogId(transmogId: Int) {
        this.transmogId = transmogId
        addBlock(UpdateBlockType.APPEARANCE)
    }

    fun hasMoveDestination(): Boolean = futureRoute != null || movementQueue.hasDestination()

    fun stopMovement() {
        movementQueue.clear()
    }

    fun getCentreTile(): Tile = tile.transform(getSize() shr 1, getSize() shr 1)

    /**
     * Gets the tile the pawn is currently facing towards.
     */
    // Credits: Kris#1337
    fun getFrontFacingTile(target: Tile, offset: Int = 0): Tile {
        val size = (getSize() shr 1)
        val centre = getCentreTile()

        val granularity = 2048
        val lutFactor = (granularity / (Math.PI * 2)) // Lookup table factor

        val theta = Math.atan2((target.z - centre.z).toDouble(), (target.x - centre.x).toDouble())
        var angle = Math.toDegrees((((theta * lutFactor).toInt() + offset) and (granularity - 1)) / lutFactor)
        if (angle < 0) {
            angle += 360
        }
        angle = Math.toRadians(angle)

        val tx = Math.round(centre.x + (size * Math.cos(angle))).toInt()
        val tz = Math.round(centre.z + (size * Math.sin(angle))).toInt()
        return Tile(tx, tz, tile.height)
    }

    /**
     * Alias for [getFrontFacingTile] using a [Pawn] as the target tile.
     */
    fun getFrontFacingTile(target: Pawn, offset: Int = 0): Tile = getFrontFacingTile(target.getCentreTile(), offset)

    /**
     * Initiate combat with [target].
     */
    fun attack(target: Pawn) {
        resetInteractions()
        interruptQueues()

        attr[COMBAT_TARGET_FOCUS_ATTR] = WeakReference(target)

        /*
         * Players always have the default combat, and npcs will use default
         * combat <strong>unless</strong> they have a custom npc combat plugin
         * bound to their npc id.
         */
        if (entityType.isPlayer() || this is Npc && !world.plugins.executeNpcCombat(this)) {
            world.plugins.executeCombat(this)
        }
    }

    fun addHit(hit: Hit) {
        pendingHits.add(hit)
    }

    fun clearHits() {
        pendingHits.clear()
    }

    /**
     * Handle a single cycle for [timers].
     */
    fun timerCycle() {
        val iterator = timers.getTimers().iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val time = entry.value
            if (time <= 0) {
                if (key == RESET_PAWN_FACING_TIMER) {
                    resetFacePawn()
                } else {
                    world.plugins.executeTimer(this, key)
                }
                if (!timers.has(key)) {
                    iterator.remove()
                }
            }
        }

        timers.getTimers().entries.forEach { timer ->
            timer.setValue(timer.value - 1)
        }
    }

    /**
     * Handle a single cycle for [pendingHits].
     */
    fun hitsCycle() {
        val hitIterator = pendingHits.iterator()
        iterator@ while (hitIterator.hasNext()) {
            if (isDead()) {
                break
            }
            val hit = hitIterator.next()

            if (lock.delaysDamage()) {
                hit.damageDelay = Math.max(0, hit.damageDelay - 1)
                continue
            }

            if (hit.damageDelay-- == 0) {
                if (!hit.cancelCondition()) {
                    blockBuffer.hits.add(hit)
                    addBlock(UpdateBlockType.HITMARK)

                    for (hitmark in hit.hitmarks) {
                        val hp = getCurrentHp()
                        if (hitmark.damage > hp) {
                            hitmark.damage = hp
                        }
                        /*
                         * Only lower the pawn's hp if they do not have infinite
                         * health enabled.
                         */
                        if (INFINITE_VARS_STORAGE.get(this, InfiniteVarsType.HP) == 0) {
                            setCurrentHp(hp - hitmark.damage)
                        }
                        /*
                         * If the pawn has less than or equal to 0 health,
                         * terminate all queues and begin the death logic.
                         */
                        if (getCurrentHp() <= 0) {
                            hit.actions.forEach { action -> action() }
                            interruptQueues()
                            if (entityType.isPlayer()) {
                                executePlugin(PlayerDeathAction.deathPlugin)
                            } else {
                                executePlugin(NpcDeathAction.deathPlugin)
                            }
                            hitIterator.remove()
                            break@iterator
                        }
                    }
                    hit.actions.forEach { action -> action() }
                }
                hitIterator.remove()
            }
        }
        if (isDead() && pendingHits.isNotEmpty()) {
            pendingHits.clear()
        }
    }

    /**
     * Handle the [futureRoute] if necessary.
     */
    fun handleFutureRoute() {
        if (futureRoute?.completed == true && futureRoute?.strategy?.cancel == false) {
            val futureRoute = futureRoute!!
            walkPath(futureRoute.route.path, futureRoute.stepType)
            this.futureRoute = null
        }
    }

    /**
     * Walk to all the tiles specified in our [path] queue, using [stepType] as
     * the [MovementQueue.StepType].
     */
    fun walkPath(path: Queue<Tile>, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL) {
        if (path.isEmpty()) {
            if (this is Player) {
                write(SetMapFlagMessage(255, 255))
            }
            return
        }

        if (timers.has(FROZEN_TIMER)) {
            if (this is Player) {
                message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
            }
            return
        }

        if (timers.has(STUN_TIMER)) {
            return
        }

        movementQueue.clear()

        var tail: Tile? = null
        var next = path.poll()
        while (next != null) {
            movementQueue.addStep(next, stepType)
            val poll = path.poll()
            if (poll == null) {
                tail = next
            }
            next = poll
        }

        /*
         * If the tail is null (should never be unless we mess with code above), or
         * if the tail is the tile we're standing on, then we don't have to move at all!
         */
        if (tail == null || tail.sameAs(tile)) {
            if (this is Player) {
                write(SetMapFlagMessage(255, 255))
            }
            movementQueue.clear()
            return
        }

        if (this is Player && lastKnownRegionBase != null) {
            write(SetMapFlagMessage(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
        }
    }

    fun walkTo(tile: Tile, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
               projectilePath: Boolean = false) = walkTo(tile.x, tile.z, stepType, projectilePath)

    fun walkTo(x: Int, z: Int, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
               projectilePath: Boolean = false) {
        /*
         * Already standing on requested destination.
         */
        if (tile.x == x && tile.z == z) {
            return
        }

        if (timers.has(FROZEN_TIMER)) {
            if (this is Player) {
                message(Entity.MAGIC_STOPS_YOU_FROM_MOVING)
            }
            return
        }

        if (timers.has(STUN_TIMER)) {
            return
        }

        val multiThread = world.multiThreadPathFinding
        val request = PathRequest.createWalkRequest(this, x, z, projectilePath)
        val strategy = createPathFindingStrategy(copyChunks = multiThread)

        /*
         * When using multi-thread path-finding, the [PathRequest.createWalkRequest]
         * must have the [tile] in sync with the game-thread, so we need to make sure
         * that in this cycle, the pawn's [tile] does not change. The easiest way to
         * do this is by clearing their movement queue. Though it can cause weird
         */
        if (multiThread) {
            movementQueue.clear()
        }
        futureRoute?.strategy?.cancel = true

        if (multiThread) {
            futureRoute = FutureRoute.of(strategy, request, stepType)
        } else {
            val route = strategy.calculateRoute(request)
            walkPath(route.path, stepType)
        }
    }

    suspend fun walkTo(it: QueueTask, tile: Tile, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
                       projectilePath: Boolean = false) = walkTo(it, tile.x, tile.z, stepType, projectilePath)

    suspend fun walkTo(it: QueueTask, x: Int, z: Int, stepType: MovementQueue.StepType = MovementQueue.StepType.NORMAL,
                       projectilePath: Boolean = false): Route {
        /*
         * Already standing on requested destination.
         */
        if (tile.x == x && tile.z == z) {
            return Route(ArrayDeque(), success = true, tail = Tile(tile))
        }
        val multiThread = world.multiThreadPathFinding
        val request = PathRequest.createWalkRequest(this, x, z, projectilePath)
        val strategy = createPathFindingStrategy(copyChunks = multiThread)

        if (multiThread) {
            movementQueue.clear()
        }
        movementQueue.clear()
        futureRoute?.strategy?.cancel = true

        if (multiThread) {
            futureRoute = FutureRoute.of(strategy, request, stepType)
            while (!futureRoute!!.completed) {
                it.wait(1)
            }
            return futureRoute!!.route
        }

        val route = strategy.calculateRoute(request)
        walkPath(route.path, stepType)
        return route
    }

    fun moveTo(x: Int, z: Int, height: Int = 0) {
        teleport = true
        tile = Tile(x, z, height)
        movementQueue.clear()
        addBlock(UpdateBlockType.MOVEMENT)
    }

    fun moveTo(tile: Tile) {
        moveTo(tile.x, tile.z, tile.height)
    }

    fun animate(id: Int) {
        blockBuffer.animation = id
        addBlock(UpdateBlockType.ANIMATION)
    }

    fun graphic(id: Int, height: Int = 0, delay: Int = 0) {
        blockBuffer.graphicId = id
        blockBuffer.graphicHeight = height
        blockBuffer.graphicDelay = delay
        addBlock(UpdateBlockType.GFX)
    }

    fun graphic(graphic: Graphic) {
        graphic(graphic.id, graphic.height, graphic.delay)
    }

    fun forceChat(message: String) {
        blockBuffer.forceChat = message
        addBlock(UpdateBlockType.FORCE_CHAT)
    }

    fun faceTile(face: Tile, width: Int = 1, length: Int = 1) {
        if (entityType.isPlayer()) {
            val srcX = tile.x * 64
            val srcZ = tile.z * 64
            val dstX = face.x * 64
            val dstZ = face.z * 64

            var degreesX = (srcX - dstX).toDouble()
            var degreesZ = (srcZ - dstZ).toDouble()

            degreesX += (Math.floor(width / 2.0)) * 32
            degreesZ += (Math.floor(length / 2.0)) * 32

            blockBuffer.faceDegrees = (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff
        } else if (entityType.isNpc()) {
            blockBuffer.faceDegrees = (face.x shl 16) or face.z
        }

        blockBuffer.facePawnIndex = -1
        addBlock(UpdateBlockType.FACE_TILE)
    }

    fun facePawn(pawn: Pawn) {
        blockBuffer.faceDegrees = 0

        val index = if (pawn.entityType.isPlayer()) pawn.index + 32768 else pawn.index
        if (blockBuffer.facePawnIndex != index) {
            blockBuffer.faceDegrees = 0
            blockBuffer.facePawnIndex = index
            addBlock(UpdateBlockType.FACE_PAWN)
        }

        attr[FACING_PAWN_ATTR] = WeakReference(pawn)
    }

    fun resetFacePawn() {
        blockBuffer.faceDegrees = 0

        val index = -1
        if (blockBuffer.facePawnIndex != index) {
            blockBuffer.faceDegrees = 0
            blockBuffer.facePawnIndex = index
            addBlock(UpdateBlockType.FACE_PAWN)
        }

        attr.remove(FACING_PAWN_ATTR)
    }

    /**
     * Resets any interaction this pawn had with another pawn.
     */
    fun resetInteractions() {
        attr.remove(COMBAT_TARGET_FOCUS_ATTR)
        attr.remove(INTERACTING_NPC_ATTR)
        attr.remove(INTERACTING_PLAYER_ATTR)
        resetFacePawn()
    }

    fun queue(priority: TaskPriority = TaskPriority.STANDARD, logic: suspend QueueTask.(CoroutineScope) -> Unit) {
        queues.queue(this, world.coroutineDispatcher, priority, logic)
    }

    /**
     * Terminates any on-going [QueueTask]s that are being executed by this [Pawn].
     */
    fun interruptQueues() {
        queues.terminateTasks()
    }

    /**
     * Executes a plugin with this [Pawn] as its context.
     */
    fun <T> executePlugin(logic: Plugin.() -> T): T {
        val plugin = Plugin(this)
        return logic(plugin)
    }

    fun triggerEvent(event: Event) {
        world.plugins.executeEvent(this, event)
        world.getService(LoggerService::class.java, searchSubclasses = true)?.logEvent(this, event)
    }

    internal fun createPathFindingStrategy(copyChunks: Boolean = false): PathFindingStrategy {
        val collision: CollisionManager = if (copyChunks) {
            val chunks = world.chunks.copyChunksWithinRadius(tile.chunkCoords, height = tile.height, radius = Chunk.CHUNK_VIEW_RADIUS)
            CollisionManager(chunks, createChunksIfNeeded = false)
        } else {
            world.collision
        }
        return if (entityType.isPlayer()) BFSPathFindingStrategy(collision) else SimplePathFindingStrategy(collision)
    }
}
