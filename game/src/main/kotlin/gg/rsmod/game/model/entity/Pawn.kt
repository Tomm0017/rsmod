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
     * The current [LockState] which filters what actions this [Pawn] can perform.
     */
    var lock = LockState.NONE

    /**
     * The attributes attached to the [Pawn].
     *
     * @see AttributeSystem
     */
    val attr = AttributeSystem()

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    abstract fun isDead(): Boolean

    abstract fun isRunning(): Boolean

    override fun setTile(t: Tile) {
        if (!getTile().sameAs(0, 0)) {
            val oldChunk = world.regionChunks.getChunkForTile(getTile())
            oldChunk.removeEntity(world, this)
        }

        super.setTile(t)

        if (!getTile().sameAs(0, 0)) {
            val newChunk = world.regionChunks.getChunkForTile(getTile())
            newChunk.addEntity(world, this)
        }
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
        if (validSurroundingTiles != null && getTile() in validSurroundingTiles) {
            return
        }

        val path = BFSPathfindingStrategy(world).getPath(getTile(), Tile(x, z, getTile().height), getType(), validSurroundingTiles)
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

        if (tail != null && this is Player) {
            write(SetMinimapMarkerMessage(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
        }
    }

    fun walkTo(tile: Tile, stepType: MovementQueue.StepType, validSurroundingTiles: Array<Tile>?) {
        walkTo(tile.x, tile.z, stepType, validSurroundingTiles)
    }

    fun teleport(x: Int, z: Int, height: Int = 0) {
        teleport = true
        setTile(Tile(x, z, height))
        movementQueue.clear()
        blockBuffer.addBlock(UpdateBlock.MOVEMENT, getType())
    }

    fun teleport(tile: Tile) {
        teleport(tile.x, tile.z, tile.height)
    }

    fun animate(id: Int) {
        blockBuffer.animation = id
        blockBuffer.addBlock(UpdateBlock.ANIMATION, getType())
    }

    fun graphic(id: Int, height: Int = 0, delay: Int = 0) {
        blockBuffer.graphicId = id
        blockBuffer.graphicHeight = height
        blockBuffer.graphicDelay = delay
        blockBuffer.addBlock(UpdateBlock.GFX, getType())
    }

    fun forceChat(message: String) {
        blockBuffer.forceChat = message
        blockBuffer.addBlock(UpdateBlock.FORCE_CHAT, getType())
    }

    fun faceTile(face: Tile, width: Int = 1, length: Int = 1) {
        if (getType().isPlayer()) {
            val srcX = getTile().x * 64
            val srcZ = getTile().z * 64
            val dstX = face.x * 64
            val dstZ = face.z * 64

            var degreesX = (srcX - dstX).toDouble()
            var degreesZ = (srcZ - dstZ).toDouble()

            degreesX += (Math.floor(width / 2.0)) * 32
            degreesZ += (Math.floor(length / 2.0)) * 32

            blockBuffer.faceDegrees = (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff
        }

        blockBuffer.facePawnIndex = -1
        blockBuffer.addBlock(UpdateBlock.FACE_TILE, getType())
    }

    fun facePawn(pawn: Pawn?) {
        blockBuffer.facePawnIndex = if (pawn == null) -1 else if (pawn.getType().isPlayer()) pawn.index + 32768 else pawn.index

        blockBuffer.faceDegrees = 0
        blockBuffer.addBlock(UpdateBlock.FACE_PAWN, getType())
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
