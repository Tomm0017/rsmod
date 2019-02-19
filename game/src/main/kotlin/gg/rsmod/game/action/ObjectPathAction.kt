package gg.rsmod.game.action

import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.Direction
import gg.rsmod.game.model.MovementQueue
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
                it.wait(1)

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
        val blockDirections = hashSetOf<Direction>()

        if (type == ObjectType.INTERACTABLE.value || type == ObjectType.DIAGONAL_INTERACTABLE.value || type == ObjectType.FLOOR_DECORATION.value) {
            if (rot == 1 || rot == 3) {
                width = def.length
                length = def.width
            }
        }

        lineOfSightRange?.let {
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

        val builder = PathRequest.Builder()
                .setPoints(pawn.tile, tile)
                .setSourceSize(pawn.getSize(), pawn.getSize())
                .setProjectilePath(lineOfSightRange != null)
                .setTargetSize(width, length)
                .clipPathNodes(node = true, link = true)
                .clipDirections(*blockDirections.toTypedArray())

        lineOfSightRange?.let { builder.setTouchRadius(lineOfSightRange) }

        // TODO(Tom): work on doors & wall objects (such as rooftop agility start or varrock museum wall displays)
        if (lineOfSightRange != null) {
            builder.clipDiagonalTiles()
        } else if (group != ObjectGroup.WALL) {
            builder.clipOverlapTiles().clipDiagonalTiles()
        }

        val route = pawn.createPathFindingStrategy().calculateRoute(builder.build())

        pawn.walkPath(route.path, MovementQueue.StepType.NORMAL)

        val last = pawn.movementQueue.peekLast()
        while (last != null && !pawn.tile.sameAs(last)) {
            it.wait(1)
        }
        return route
    }

    private fun faceObj(pawn: Pawn, obj: GameObject) {
        val def = pawn.world.definitions.get(ObjectDef::class.java, obj.id)
        val rot = obj.rot
        val type = obj.type

        var width = def.width
        var length = def.length

        if (rot == 1 || rot == 3) {
            width = def.length
            length = def.width
        }

        when (type) {
            ObjectType.LENGTHWISE_WALL.value -> {
                /**
                 * Specially logic for facing doors and walls, otherwise you
                 * end up facing the same direction the object is facing.
                 */
                val dir = when (rot) {
                    0 -> obj.tile.transform(if (pawn.tile.x == obj.tile.x) -1 else 0, 0)
                    1 -> obj.tile.transform(0, 1)
                    2 -> obj.tile.transform(1, 0)
                    else -> obj.tile.transform(0, -1)
                }
                pawn.faceTile(dir)
            }
            else -> {
                pawn.faceTile(obj.tile.transform(width shr 1, length shr 1), width, length)
            }
        }
    }
}