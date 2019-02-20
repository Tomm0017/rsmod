package gg.rsmod.game.action

import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.attr.INTERACTING_OBJ_ATTR
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.collision.ObjectGroup
import gg.rsmod.game.model.collision.ObjectType
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.path.PathRequest
import gg.rsmod.game.model.path.Route
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.util.DataConstants
import java.util.*

/**
 * This class is eesponsible for calculating distances and valid interaction
 * tiles for [GameObject] path-finding.
 *
 * @author Tom <rspsmods@gmail.com>
 */
object ObjectPathAction {

    val walkPlugin: (Plugin) -> Unit = {
        val player = it.ctx as Player
        val obj = player.attr[INTERACTING_OBJ_ATTR]!!.get()!!
        val opt = player.attr[INTERACTING_OPT_ATTR]!!
        val lineOfSightRange = player.world.plugins.getObjInteractionDistance(obj.id)

        it.suspendable {
            val route = walkTo(it, obj, lineOfSightRange)
            if (lineOfSightRange == null) {
                faceObj(player, obj)
            }
            if (route.success) {
                if (!player.world.plugins.executeObject(player, obj.getTransform(player), opt)) {
                    player.message(Entity.NOTHING_INTERESTING_HAPPENS)
                    if (player.world.devContext.debugObjects) {
                        player.message("Unhandled object action: [opt=$opt, id=${obj.id}, type=${obj.type}, rot=${obj.rot}, x=${obj.tile.x}, z=${obj.tile.z}]")
                    }
                }
            } else {
                player.message(Entity.YOU_CANT_REACH_THAT)
            }
        }
    }

    private suspend fun walkTo(it: Plugin, obj: GameObject, lineOfSightRange: Int?): Route {
        val pawn = it.ctx as Pawn

        val def = obj.getDef(pawn.world.definitions)
        val tile = obj.tile
        val type = obj.type
        val rot = obj.rot
        var width = def.width
        var length = def.length
        val clipMask = def.clipMask

        val group = ObjectType.values.first { it.value == type }.group
        val wall = group == ObjectGroup.WALL_DECORATION || group == ObjectGroup.WALL
        val blockDirections = EnumSet.noneOf(Direction::class.java)

        if (!wall && (rot == 1 || rot == 3)) {
            width = def.length
            length = def.width
        }

        if (lineOfSightRange != null) {
            width = Math.max(width, lineOfSightRange)
            length = Math.max(length, lineOfSightRange)
        }

        val blockBits = 4
        val clipFlag = (DataConstants.BIT_MASK[blockBits] and (clipMask shl rot)) or (clipMask shr (blockBits - rot))

        if ((0x1 and clipFlag) != 0) {
            blockDirections.add(Direction.NORTH)
        }

        if ((0x2 and clipFlag) != 0) {
            blockDirections.add(Direction.EAST)
        }

        if ((0x4 and clipFlag) != 0) {
            blockDirections.add(Direction.SOUTH)
        }

        if ((clipFlag and 0x8) != 0) {
            blockDirections.add(Direction.WEST)
        }

        val wallUnreachable = when (rot) {
            0 -> EnumSet.of(Direction.EAST)
            1 -> EnumSet.of(Direction.SOUTH)
            2 -> EnumSet.of(Direction.WEST)
            3 -> EnumSet.of(Direction.NORTH)
            else -> throw IllegalStateException("Invalid object rotation: $rot")
        }

        if (wall) {
            if (pawn.tile.isWithinRadius(tile, 1)) {
                val dir = Direction.between(tile, pawn.tile)
                if (dir !in wallUnreachable && !dir.isDiagonal()) {
                    return Route(ArrayDeque(), success = true, tail = pawn.tile)
                }
            }

            blockDirections.addAll(wallUnreachable)
        }

        val builder = PathRequest.Builder()
                .setPoints(pawn.tile, tile)
                .setSourceSize(pawn.getSize(), pawn.getSize())
                .setProjectilePath(lineOfSightRange != null)
                .setTargetSize(width, length)
                .clipDiagonalTiles()
                .clipPathNodes(node = true, link = true)
                .clipDirections(*blockDirections.toTypedArray())

        if (lineOfSightRange != null) {
            builder.setTouchRadius(lineOfSightRange)
        }

        if (!wall) {
            builder.clipOverlapTiles()
        }

        val route = pawn.createPathFindingStrategy().calculateRoute(builder.build())
        pawn.walkPath(route.path)

        val last = pawn.movementQueue.peekLast()
        while (last != null && !pawn.tile.sameAs(last)) {
            it.wait(1)
        }

        if (wall && !route.success && Direction.between(tile, pawn.tile) !in wallUnreachable) {
            return Route(route.path, success = true, tail = route.tail)
        }

        return route
    }

    private fun faceObj(pawn: Pawn, obj: GameObject) {
        val def = pawn.world.definitions.get(ObjectDef::class.java, obj.id)
        val rot = obj.rot
        val type = obj.type

        when (type) {
            ObjectType.LENGTHWISE_WALL.value -> {
                if (!pawn.tile.sameAs(obj.tile)) {
                    pawn.faceTile(obj.tile)
                }
            }
            else -> {
                var width = def.width
                var length = def.length
                if (rot == 1 || rot == 3) {
                    width = def.length
                    length = def.width
                }
                pawn.faceTile(obj.tile.transform(width shr 1, length shr 1), width, length)
            }
        }
    }
}