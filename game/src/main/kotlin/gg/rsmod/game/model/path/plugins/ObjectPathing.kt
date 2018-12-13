package gg.rsmod.game.model.path.plugins

import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.*
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.plugin.Plugin

/**
 * @author Tom <rspsmods@gmail.com>
 */
object ObjectPathing {

    val walkPlugin: (Plugin) -> Unit = {
        val p = it.player()
        val obj = p.attr[INTERACTING_OBJ_ATTR]
        val opt = p.attr[INTERACTING_OPT_ATTR]
        p.walkTo(obj.tile.x, obj.tile.z, MovementQueue.StepType.NORMAL)
        it.suspendable {
            awaitArrival(it, obj, opt)
        }
    }

    private suspend fun awaitArrival(it: Plugin, obj: GameObject, opt: Int) {
        val p = it.player()
        val destination = p.movementQueue.peekLast()
        if (destination == null) {
            p.message(Entity.NOTHING_INTERESTING_HAPPENS)
            return
        }
        while (true) {
            if (!p.tile.sameAs(destination)) {
                it.wait(1)
                continue
            }

            if (!p.world.plugins.executeObject(p, obj.id, opt)) {
                p.message(Entity.NOTHING_INTERESTING_HAPPENS)
                if (p.world.gameContext.devMode) {
                    p.message("Unhandled object action: [opt=$opt, id=${obj.id}, x=${obj.tile.x}, z=${obj.tile.z}]")
                }
            }
            break
        }
    }

    fun getInteractTile(world: World, obj: GameObject): Tile {
        val def = world.definitions.get(ObjectDef::class.java, obj.id)
        val type = obj.type
        val rot = obj.rot
        var width = def.width
        var length = def.length

        /*if (type == ObjectType.LENGTHWISE_WALL.value) {
            val validTiles = arrayListOf<Tile>()

            if (rot == 0) {
                // Facing west
                for (i in 0 until length) {
                    val tile = obj.tile.transform(-1, i)
                    if ()
                    validTiles.add(tile)
                }
            }
        } else {
            val flag = def.clipFlag
            val blockBits = 4

            if (rot == 1 || rot == 3) {
                width = def.length
                length = def.width
            }

            val blockedDirections = (DataConstants.BIT_MASK[blockBits] and (flag shl rot)) + (flag shr (blockBits - rot))
            println("blocked=$blockedDirections, flag=$flag, rot=$rot, type=$type width=$width, length=$length, tile=${obj.tile}")

            val validTiles = arrayListOf<Tile>()

            if ((blockedDirections and 0x7) != 0) {
                // can interact from west

                for (z in 0 until length) {
                    val tile = obj.tile.transform(-width, z)
                    if (world.collision.canTraverse(obj.tile, Direction.WEST, EntityType.PLAYER)) {
                        validTiles.add(tile)
                    }
                }
            }
            if ((blockedDirections and 0xe) != 0) {
                // can interact from north

                for (x in 0 until width) {
                    val tile = obj.tile.transform(x, length)
                    if (world.collision.canTraverse(obj.tile, Direction.NORTH, EntityType.PLAYER)) {
                        validTiles.add(tile)
                    }
                }
            }
            if (blockedDirections == 0) {
                if (rot == 1 || rot == 3) {
                    for (x in 0 until width) {
                        val face = obj.tile.transform(x, length - 1)

                        val north = obj.tile.transform(x, length)
                        if (world.collision.isBlocked(face, Direction.NORTH, false)) {
                            validTiles.add(north)
                        }

                        val south = obj.tile.transform(x, -1)
                        if (world.collision.isBlocked(face, Direction.SOUTH, false)) {
                            validTiles.add(south)
                        }
                    }
                } else {
                    *//*for (z in 0 until length) {
                    val west = obj.tile.transform(-1, z)
                    if (world.collision.canTraverseFromAny(west, EntityType.PLAYER, Direction.WEST, Direction.NORTH, Direction.WEST)) {
                        validTiles.add(west)
                    }

                    val east = obj.tile.transform(width - 1, z)
                    if (world.collision.canTraverseFromAny(east, EntityType.PLAYER, Direction.SOUTH, Direction.EAST, Direction.WEST)) {
                        validTiles.add(east)
                    }
                }*//*
                }
                *//*val east = obj.tile.transform(x, length)
            val south = obj.tile.transform(x, length)
            val west = obj.tile.transform(x, length)*//*
            }

            println("Valid interaction tiles: ")
            validTiles.forEach { tile ->
                println("\t$tile")
            }
        }*/

        return obj.tile
    }
}