package gg.rsmod.game.model.entity

import gg.rsmod.game.message.impl.SetMinimapMarkerMessage
import gg.rsmod.game.model.*
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.strategy.BFSPathfindingStrategy
import gg.rsmod.game.model.region.Chunk
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

    var blockBuffer = UpdateBlockBuffer()

    /**
     * The 3D [Tile] that this [Pawn] was standing on, in the last game cycle.
     */
    var lastTile: Tile? = null

    var chunk: Chunk? = null

    /**
     * Whether or not this pawn can teleported this game cycle.
     */
    var teleport = false

    val movementQueue by lazy { MovementQueue(this) }

    /**
     * The current directions that this [Pawn] is moving.
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

    var currentPath: PathRequest? = null

    /**
     * Handles logic before any synchronization tasks are executed.
     */
    abstract fun cycle()

    abstract fun isDead(): Boolean

    abstract fun isRunning(): Boolean

    fun canMove(): Boolean = !isDead() && lock.canMove()

    fun walkTo(x: Int, z: Int, stepType: MovementQueue.StepType, validSurroundingTiles: Array<Tile>? = null) {
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

        if (tail != null && this is Player) {
            write(SetMinimapMarkerMessage(tail.x - lastKnownRegionBase!!.x, tail.z - lastKnownRegionBase!!.z))
        }
    }

    fun teleport(x: Int, z: Int, height: Int = 0) {
        teleport = true
        tile = Tile(x, z, height)
        movementQueue.clear()
    }

    fun teleport(tile: Tile) {
        teleport(tile.x, tile.z, tile.height)
    }

    fun forceChat(message: String) {
        if (this is Player) {
            blockBuffer.forceChat = message
            blockBuffer.addBlock(UpdateBlock.FORCE_CHAT)
        }
    }

    fun faceTile(face: Tile, width: Int = 1, length: Int = 1) {
        if (this is Player) {
            val srcX = tile.x * 64
            val srcZ = tile.z * 64
            val dstX = face.x * 64
            val dstZ = face.z * 64

            var degreesX = (srcX - dstX).toDouble()
            var degreesZ = (srcZ - dstZ).toDouble()

            degreesX += (Math.floor(width / 2.0)) * 32
            degreesZ += (Math.floor(length / 2.0)) * 32

            blockBuffer.faceDegrees = (Math.atan2(degreesX, degreesZ) * 325.949).toInt() and 0x7ff
            blockBuffer.addBlock(UpdateBlock.FACE_TILE)
        }
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
