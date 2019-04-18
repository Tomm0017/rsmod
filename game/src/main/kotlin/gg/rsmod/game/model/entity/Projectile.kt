package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile

/**
 * A projectile that can be shot in the world.
 *
 * @param targetPawn
 * The [Pawn] that is being targeted by the projectile. Can be set to null if a
 * [Tile] is the target instead.
 *
 * @param targetTile
 * The [Tile] where the projectile will land. Can be set to null if [Pawn] is
 * being the target instead.
 *
 * @param gfx
 * The graphic id for the projectile (aka map projectile animation).
 *
 * @param startHeight
 * The visual starting height of the projectile.
 *
 * @param endHeight
 * The visual end height of the projectile.
 *
 * @param steepness
 * The visual steepness of the projectile.
 *
 * @param angle
 * The visual angle of the projectile.
 *
 * @param delay
 * The delay before the projectile is spawned in players' clients, in client time.
 *
 * @param lifespan
 * The amount of time that the projectile will stay in players' client, in client time.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Projectile private constructor(val targetPawn: Pawn?, val targetTile: Tile,
                                     val gfx: Int, val startHeight: Int, val endHeight: Int,
                                     val steepness: Int, val angle: Int, val delay: Int, val lifespan: Int) : Entity() {

    override val entityType: EntityType = EntityType.PROJECTILE

    override fun toString(): String = MoreObjects.toStringHelper(this).add("start", tile).add("targetPawn", targetPawn)
            .add("targetTile", targetTile).add("gfx", gfx).add("startHeight", startHeight)
            .add("endHeight", endHeight).add("angle", angle).add("steepness", steepness)
            .add("delay", delay).add("lifespan", lifespan).toString()

    class Builder {

        private var start: Tile? = null

        private var targetPawn: Pawn? = null

        private var targetTile: Tile? = null

        private var gfx = -1

        private var startHeight = -1

        private var endHeight = -1

        private var angle = -1

        private var steepness = -1

        private var delay = -1

        private var lifespan = -1

        fun build(): Projectile {
            checkNotNull(start) { "Start must be set." }
            check(targetPawn != null || targetTile != null) { "Target must be set." }
            check(gfx != -1) { "Gfx must be set." }

            val start = start!!
            val target = targetPawn?.tile ?: targetTile!!

            if (startHeight == -1) {
                startHeight = 0
            }

            if (endHeight == -1) {
                endHeight = 0
            }

            if (angle == -1) {
                angle = 0
            }

            if (steepness == -1) {
                steepness = 0
            }

            if (delay == -1) {
                delay = 0
            }

            if (lifespan == -1) {
                lifespan = start.getDistance(target) * 5
            }

            val projectile = Projectile(targetPawn, target, gfx, startHeight, endHeight, steepness, angle, delay, lifespan)
            projectile.tile = start
            return projectile
        }

        fun setStart(start: Tile): Builder {
            this.start = start
            return this
        }

        fun setTarget(pawn: Pawn): Builder {
            this.targetPawn = pawn
            return this
        }

        fun setTarget(tile: Tile): Builder {
            this.targetTile = tile
            return this
        }

        fun setGfx(gfx: Int): Builder {
            this.gfx = gfx
            return this
        }

        fun setStartHeight(startHeight: Int): Builder {
            this.startHeight = startHeight
            return this
        }

        fun setEndHeight(endHeight: Int): Builder {
            this.endHeight = endHeight
            return this
        }

        fun setAngle(angle: Int): Builder {
            this.angle = angle
            return this
        }

        fun setSteepness(steepness: Int): Builder {
            this.steepness = steepness
            return this
        }

        fun setDelay(delay: Int): Builder {
            this.delay = delay
            return this
        }

        fun setLifespan(lifespan: Int): Builder {
            this.lifespan = lifespan
            return this
        }

        fun setTiles(start: Tile, target: Tile): Builder {
            this.start = start
            this.targetTile = target
            return this
        }

        fun setTiles(start: Tile, target: Pawn): Builder {
            this.start = start
            this.targetPawn = target
            return this
        }

        fun setHeights(startHeight: Int, endHeight: Int): Builder {
            this.startHeight = startHeight
            this.endHeight = endHeight
            return this
        }

        fun setSlope(angle: Int, steepness: Int): Builder {
            this.angle = angle
            this.steepness = steepness
            return this
        }

        fun setTimes(delay: Int, lifespan: Int): Builder {
            this.delay = delay
            this.lifespan = lifespan
            return this
        }
    }
}