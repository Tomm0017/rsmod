package gg.rsmod.game.model.entity

import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.path.strategy.BFSPathfindingStrategy
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.game.sync.UpdateBlock
import gg.rsmod.game.sync.UpdateBlockBuffer

/**
 * A controllable character in the world that is used by something, or someone,
 * for their own purpose.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class Pawn(open val world: World) : Entity() {

    /**
     * The index assigned when this [Pawn] is successfully added to a [PawnList].
     */
    var index = -1

    /**
     * @see UpdateBlockBuffer
     */
    var blockBuffer = UpdateBlockBuffer()

    /**
     * The 3D [Tile] that this pawn was standing on, in the last game cycle.
     */
    var lastTile: Tile? = null

    /**
     * Whether or not this pawn can teleported this game cycle.
     */
    var teleport = false

    /**
     * @see [MovementQueue]
     */
    val movementQueue by lazy { MovementQueue(this) }

    /**
     * The current directions that this pawn is moving.
     */
    var steps: MovementQueue.StepDirection? = null

    /**
     * The last [Direction] this pawn was facing.
     */
    var lastFacingDirection: Direction = Direction.NONE

    /**
     * The current [LockState] which filters what actions this pawn can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the pawn.
     *
     * @see AttributeSystem
     */
    val attr = AttributeSystem()

    /**
     * The timers attached to the pawn.
     *
     * @see TimerSystem
     */
    val timers = TimerSystem()

    /**
     * The current [PrayerIcon] that the pawn has active.
     */
    var prayerIcon = PrayerIcon.NONE

    /**
     * Transmog is the action of turning into an npc. This value is equal to the
     * npc id of the npc you want to turn into, visually.
     */
    private var transmogId = -1

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    abstract fun isDead(): Boolean

    abstract fun isRunning(): Boolean
    
    abstract fun addBlock(block: UpdateBlock)

    abstract fun hasBlock(block: UpdateBlock): Boolean

    fun getTransmogId(): Int = transmogId

    fun setTransmogId(transmogId: Int) {
        this.transmogId = transmogId
        addBlock(UpdateBlock.APPEARANCE)
    }

    fun canMove(): Boolean = !isDead() && lock.canMove()

    /**
     * Handles the walking to the specified [x] and [z] coordinates. The height
     * level used is the one this pawn is currently on.
     *
     * @param stepType
     * The [MovementQueue.StepType] that the movement to the coordinates will
     * use.
     *
     * @param validSurroundingTiles
     * If we have a list of predetermined [Tile]s, we set this value to that list.
     * This is useful for pathfinding on things like objects, where the object
     * has metadata which defines the surrounding tiles that it can be interacted
     * from.
     */
    fun walkTo(x: Int, z: Int, stepType: MovementQueue.StepType, validSurroundingTiles: Array<Tile>? = null) {
        /**
         * This will cause desync since the player is already on the tile,
         * they should not be able to add a step in a tile they are already
         * standing on.
         */
        if (validSurroundingTiles != null && tile in validSurroundingTiles) {
            return
        }

        val path = BFSPathfindingStrategy(world).getPath(tile, Tile(x, z, tile.height), getType(), validSurroundingTiles)
        if (path.isEmpty() && this is Player) {
            write(SetMinimapMarkerMessage(255, 255))
            return
        }

        var tail: Tile? = null

        movementQueue.clear()
        var next = path.poll()
        while (next != null) {
            movementQueue.addStep(next, stepType)
            val poll = path.poll()
            if (poll == null) {
                tail = next
            }
            next = poll
        }

        if (tail != null && this is Player && lastKnownRegionBase != null) {
            write(SetMinimapMarkerMessage(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
        }
    }

    fun walkTo(tile: Tile, stepType: MovementQueue.StepType, validSurroundingTiles: Array<Tile>?) {
        walkTo(tile.x, tile.z, stepType, validSurroundingTiles)
    }

    fun teleport(x: Int, z: Int, height: Int = 0) {
        teleport = true
        tile = Tile(x, z, height)
        movementQueue.clear()
        addBlock(UpdateBlock.MOVEMENT)
    }

    fun teleport(tile: Tile) {
        teleport(tile.x, tile.z, tile.height)
    }

    fun animate(id: Int) {
        blockBuffer.animation = id
        addBlock(UpdateBlock.ANIMATION)
    }

    fun graphic(id: Int, height: Int = 0, delay: Int = 0) {
        blockBuffer.graphicId = id
        blockBuffer.graphicHeight = height
        blockBuffer.graphicDelay = delay
        addBlock(UpdateBlock.GFX)
    }

    fun forceChat(message: String) {
        blockBuffer.forceChat = message
        addBlock(UpdateBlock.FORCE_CHAT)
    }

    fun faceTile(face: Tile, width: Int = 1, length: Int = 1) {
        if (getType().isPlayer()) {
            val srcX = tile.x * 64
            val srcZ = tile.z * 64
            val dstX = face.x * 64
            val dstZ = face.z * 64

            var degreesX = (srcX - dstX).toDouble()
            var degreesZ = (srcZ - dstZ).toDouble()

            degreesX += (Math.floor(width / 2.0)) * 32
            degreesZ += (Math.floor(length / 2.0)) * 32

            blockBuffer.faceDegrees = (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff
        }

        blockBuffer.facePawnIndex = -1
        addBlock(UpdateBlock.FACE_TILE)
    }

    fun facePawn(pawn: Pawn?) {
        blockBuffer.facePawnIndex = if (pawn == null) -1 else if (pawn.getType().isPlayer()) pawn.index + 32768 else pawn.index

        blockBuffer.faceDegrees = 0
        addBlock(UpdateBlock.FACE_PAWN)
    }

    /**
     * Executes a plugin with this [Pawn] as its context.
     */
    fun executePlugin(plugin: Function1<Plugin, Unit>) {
        world.pluginExecutor.execute(this, plugin)
    }

    /**
     * Terminates any on-going plugins that are being executed by this [Pawn].
     */
    fun interruptPlugins() {
        world.pluginExecutor.interruptPluginsWithContext(this)
    }
}
