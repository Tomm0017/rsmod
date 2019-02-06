package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.sync.block.UpdateBlockType
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class MovementQueue(val pawn: Pawn) {

    private val steps: Deque<Step> = ArrayDeque()

    fun hasDestination(): Boolean = steps.isNotEmpty()

    fun peekLast(): Tile? = if (steps.isNotEmpty()) steps.peekLast().tile else null

    fun clear() {
        steps.clear()
    }

    fun addStep(step: Tile, type: StepType) {
        addStep(steps.peekLast()?.tile ?: pawn.tile, step, type)
    }

    fun pulse() {
        val collision = pawn.world.collision

        var next = steps.poll()
        if (next != null) {
            var tile = pawn.tile

            var walkDirection: Direction?
            var runDirection: Direction? = null

            walkDirection = Direction.between(tile, next.tile)

            if (collision.canTraverse(tile, walkDirection, projectile = false)) {
                tile = Tile(next.tile)
                pawn.lastFacingDirection = walkDirection

                val running = when (next.type) {
                    StepType.NORMAL -> pawn.isRunning()
                    StepType.FORCED_RUN -> true
                    StepType.FORCED_WALK -> false
                }
                if (running) {
                    next = steps.poll()
                    if (next != null) {
                        runDirection = Direction.between(tile, next.tile)

                        if (collision.canTraverse(tile, runDirection, projectile = false)) {
                            tile = Tile(next.tile)
                            pawn.lastFacingDirection = runDirection
                        } else {
                            clear()
                            runDirection = null
                        }
                    }
                }
            } else {
                walkDirection = null
                clear()
            }

            if (walkDirection != null) {
                pawn.steps = StepDirection(walkDirection, runDirection)
                pawn.tile = Tile(tile)
                if (runDirection != null) {
                    pawn.addBlock(UpdateBlockType.MOVEMENT)
                }
            }
        }
    }

    private fun addStep(current: Tile, next: Tile, type: StepType) {
        var dx = next.x - current.x
        var dz = next.z - current.z
        val delta = Math.max(Math.abs(dx), Math.abs(dz))

        val chunks = pawn.world.chunks
        var chunk = chunks.getForTile(current)

        for (i in 0 until delta) {
            if (dx < 0) {
                dx++
            } else if (dx > 0) {
                dx--
            }

            if (dz < 0) {
                dz++
            } else if (dz > 0) {
                dz--
            }

            val step = next.transform(-dx, -dz)
            if (!chunk.contains(step)) {
                chunk = chunks.getForTile(step)
            }

            steps.add(Step(step, type))
        }
    }

    data class StepDirection(val walkDirection: Direction?, val runDirection: Direction?)

    data class Step(val tile: Tile, val type: StepType)

    enum class StepType {
        NORMAL,
        FORCED_WALK,
        FORCED_RUN
    }
}