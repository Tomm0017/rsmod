
import gg.rsmod.game.action.CombatPathAction
import gg.rsmod.game.model.COMBAT_TARGET_FOCUS
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.helper.pawn
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.CombatStrategy
import gg.rsmod.plugins.osrs.content.combat.strategy.RangedCombatStrategy

r.bindCombat {
    it.suspendable {
        while (true) {
            if (!cycle(it)) {
                break
            }
            it.wait(1)
        }
    }
}

suspend fun cycle(it: Plugin): Boolean {
    val pawn = it.pawn()
    val target = pawn.attr[COMBAT_TARGET_FOCUS] ?: return false

    pawn.facePawn(target)

    val strategy = getCombatStrategy(pawn)
    val attackRange = strategy.getAttackRange(-1)
    val pathFound = walkTo(it, pawn, target, attackRange)

    if (!pathFound) {
        pawn.movementQueue.clear()
        if (pawn is Player) {
            pawn.message(Entity.YOU_CANT_REACH_THAT)
        }
        return false
    }

    pawn.movementQueue.clear()

    attack(pawn, target)
    pawn.timers[Combat.ATTACK_DELAY] = 3

    return true
}

suspend fun walkTo(it: Plugin, pawn: Pawn, target: Pawn, range: Int): Boolean {
    val world = pawn.world
    val start = pawn.calculateCentreTile()
    val end = target.calculateCentreTile()

    /**
     * If the start and end tile are on the same tile, the path finder won't
     * have any path tiles anyway, so it wouldn't be accurate to use.
     */
    if (!start.sameAs(end)) {
        val pathFinder = pawn.createPathingStrategy()
        val validPath = pathFinder.getPath(start, end, pawn.getType())
        if (validPath.isEmpty()) {
            return false
        }
        var tail = validPath.poll()
        while (true) {
            val poll = validPath.poll() ?: break
            tail = poll
        }
        if (start.sameAs(tail)) {
            return false
        }
    }

    val targetRadius = Math.floor(target.getTileSize() / 2.0).toInt()

    /**
     * If the start tile is inside of the target tile, in relation to the target's
     * tile size, we want to move outwards.
     */
    if (start.isWithinRadius(end, targetRadius)) {
        if (target.getTileSize() == 1) {
            mainLoop@ while (true) {
                val order = Direction.WNES
                val validTiles = order.map { end.step(it) }
                val tile = validTiles.firstOrNull { world.collision.canTraverse(start, Direction.between(end, it), pawn.getType()) } ?: return false

                val dst = pawn.walkTo(tile.x, tile.z, MovementQueue.StepType.NORMAL) ?: return false
                while (!pawn.tile.sameAs(dst)) {
                    if (target.lastTile?.sameAs(target.tile) == false) {
                        continue@mainLoop
                    }
                    it.wait(1)
                }
            }
        } else {
            mainLoop@ while (true) {
                val validTiles = CombatPathAction.getBorderTiles(target)
                val closest = validTiles.sortedBy { tile -> tile.getDistance(pawn.tile) }.first()

                val dst = pawn.walkTo(closest.x, closest.z, MovementQueue.StepType.NORMAL, validTiles) ?: return false
                while (!pawn.tile.sameAs(dst)) {
                    if (target.lastTile?.sameAs(target.tile) == false) {
                        continue@mainLoop
                    }
                    it.wait(1)
                }
            }
        }
    }

    mainLoop@ while (true) {
        val withinRadius = start.isWithinRadius(end, range)
        val raycast = world.collision.raycast(start, end)

        if (withinRadius && raycast) {
            return true
        }

        var dst: Tile? = null

        if (targetRadius > 0) {
            val validTiles = CombatPathAction.getValidBorderTiles(target)
            if (pawn.tile !in validTiles) {
                val closest = validTiles.sortedBy { tile -> tile.getDistance(pawn.tile) }.first()
                dst = pawn.walkTo(closest.x, closest.z, MovementQueue.StepType.NORMAL, validTiles) ?: return false
            }
        } else {
            dst = pawn.walkTo(end.x, end.z, MovementQueue.StepType.NORMAL)
        }

        if (dst == null) {
            return false
        }

        while (!pawn.tile.sameAs(dst)) {
            if (target.lastTile?.sameAs(target.tile) == false) {
                continue@mainLoop
            }
            it.wait(1)
        }
        return raycast
    }
}

fun attack(pawn: Pawn, target: Pawn) {
    pawn.animate(422)
    target.facePawn(pawn)
}

fun getCombatStrategy(pawn: Pawn): CombatStrategy {
    return RangedCombatStrategy
}