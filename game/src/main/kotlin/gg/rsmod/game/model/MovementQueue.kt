package gg.rsmod.game.model

import gg.rsmod.game.model.MovementQueue.Step
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.sync.block.UpdateBlockType
import java.util.ArrayDeque
import java.util.Deque

/**
 * Responsible for handling a queue of [Step]s for a [Pawn].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MovementQueue(val pawn: Pawn) {

    /**
     * A [Deque] of steps.
     */
    private val steps: Deque<Step> = ArrayDeque()

    /**
     * If any step is queued.
     */
    fun hasDestination(): Boolean = steps.isNotEmpty()

    /**
     * Get the last tile in our [steps] without removing it.
     */
    fun peekLast(): Tile? = peekLastStep()?.tile

    fun peekLastStep(): Step? = if (steps.isNotEmpty()) steps.peekLast() else null

    fun clear() {
        steps.clear()
    }

    fun addStep(step: Tile, type: StepType, detectCollision: Boolean) {
        val current = if (steps.any()) steps.peekLast().tile else pawn.tile
        addStep(current, step, type, detectCollision)
    }

    fun cycle() {
        val collision = pawn.world.collision

        var next = steps.poll()
        var tile = pawn.tile
        if (next != null) {

            var walkDirection: Direction?
            var runDirection: Direction? = null

            walkDirection = Direction.between(tile, next.tile)

            if (walkDirection != Direction.NONE && (!next.detectCollision || collision.canTraverse(tile, walkDirection, projectile = false))) {
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

                        if (!next.detectCollision || collision.canTraverse(tile, runDirection, projectile = false)) {
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

            if (walkDirection != null && walkDirection != Direction.NONE) {
                pawn.steps = StepDirection(walkDirection, runDirection)
                if (runDirection != null) {
                    pawn.addBlock(UpdateBlockType.MOVEMENT)
                }
            }
        }
        pawn.tile = Tile(tile)
    }

    private fun addStep(current: Tile, next: Tile, type: StepType, detectCollision: Boolean) {
        var dx = next.x - current.x
        var dz = next.z - current.z
        val delta = Math.max(Math.abs(dx), Math.abs(dz))

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
            steps.add(Step(step, type, detectCollision))
        }
    }

    data class StepDirection(val walkDirection: Direction?, val runDirection: Direction?)

    data class Step(val tile: Tile, val type: StepType, val detectCollision: Boolean)

    enum class StepType {
        NORMAL,
        FORCED_WALK,
        FORCED_RUN
    }
}