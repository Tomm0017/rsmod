package gg.rsmod.game.model.path.plugins

import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.INTERACTING_OBJ_ATTR
import gg.rsmod.game.model.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.MovementQueue
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.collision.ObjectType
import gg.rsmod.game.model.entity.Entity
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.util.DataConstants

/**
 * @author Tom <rspsmods@gmail.com>
 */
object ObjectPathing {

    val walkPlugin: (Plugin) -> Unit = {
        val p = it.player()
        val obj = p.attr[INTERACTING_OBJ_ATTR]
        val opt = p.attr[INTERACTING_OPT_ATTR]
        if (!isTouching(p, obj)) {
            walkTo(p, obj)
            it.suspendable {
                awaitArrival(it, obj, opt)
            }
        } else {
            it.suspendable {
                faceObj(p, obj)
                handleAction(it, obj, opt)
            }
        }
    }

    private fun walkTo(p: Player, obj: GameObject) {
        val def = p.world.definitions[ObjectDef::class.java][obj.id]
        val rot = obj.rot
        val flag = def.clipFlag
        var width = def.width
        var length = def.length
        var x = obj.tile.x
        var z = obj.tile.z

        val blockBits = 4
        val blockedDirections = (DataConstants.BIT_MASK[blockBits] and (flag shl rot)) + (flag shr (blockBits - rot))

        /**
         * The dimensions are swapped when the object has any of these rotations.
         */
        if (rot == 1 || rot == 3) {
            width = def.length
            length = def.width
        }

        /**
         * Can only interact from West side.
         */
        if ((blockedDirections and 0x7) == 0x7) {
            x -= 1
        }

        /**
         * Can only interact from North side.
         */
        if ((blockedDirections and 0xe) == 0xe) {
            z += 1
            if (p.tile.x - obj.tile.x in 1..width) {
                x = obj.tile.x + (width - 1)
            }
        }

        /**
         * Can only interact from South side.
         */
        if ((blockedDirections and 0xb) == 0xb || rot == 1 && width > 1) {
            z -= 1
            if (p.tile.x - obj.tile.x in 1..width) {
                x = obj.tile.x + (width - 1)
            }
        }

        println("walk to: $x, $z")

        p.walkTo(x, z, MovementQueue.StepType.NORMAL, width, length)
    }

    private suspend fun awaitArrival(it: Plugin, obj: GameObject, opt: Int) {
        val p = it.player()
        val destination = p.movementQueue.peekLast()
        if (destination == null) {
            p.message(Entity.YOU_CANT_REACH_THAT)
            return
        }
        while (true) {
            if (!p.tile.sameAs(destination)) {
                it.wait(1)
                continue
            }
            faceObj(p, obj)
            it.wait(1)
            if (isTouching(p, obj)) {
                handleAction(it, obj, opt)
            } else {
                p.message(Entity.YOU_CANT_REACH_THAT)
            }
            break
        }
    }

    private fun faceObj(p: Player, obj: GameObject) {
        val def = p.world.definitions.get(ObjectDef::class.java, obj.id)

        when (obj.type) {
            ObjectType.LENGTHWISE_WALL.value -> {
                /**
                 * Doors and walls, otherwise you end up facing the same direction
                 * the object is facing.
                 */
                val dir = when (obj.rot) {
                    0 -> obj.tile.transform(if (p.tile.x == obj.tile.x) -1 else 0, 0)
                    1 -> obj.tile.transform(0, 1)
                    2 -> obj.tile.transform(1, 0)
                    else -> obj.tile.transform(0, -1)
                }
                p.faceTile(dir)
            }
            else -> {
                val face = when {
                    /**
                     * For anything that's does not have a width/length of 2, we
                     * will try to face it's center.
                     */
                    def.width != 2 && def.length != 2 -> obj.tile.transform(def.width shr 1, def.length shr 1)
                    /**
                     * If the object has a dimension of two units, we will try to
                     * face the dimension closest to us.
                     */
                    else -> if (p.tile.x != obj.tile.x) Tile(p.tile.x, obj.tile.z) else obj.tile
                }
                p.faceTile(face)
            }
        }
    }

    private fun handleAction(it: Plugin, obj: GameObject, opt: Int) {
        val p = it.player()

        if (!p.world.plugins.executeObject(p, obj.id, opt)) {
            p.message(Entity.NOTHING_INTERESTING_HAPPENS)
            if (p.world.gameContext.devMode) {
                p.message("Unhandled object action: [opt=$opt, id=${obj.id}, type=${obj.type}, rot=${obj.rot}, x=${obj.tile.x}, z=${obj.tile.z}]")
            }
        }
    }

    private fun isTouching(p: Player, obj: GameObject): Boolean {
        val world = p.world
        val def = world.definitions.get(ObjectDef::class.java, obj.id)
        val type = obj.type
        val rot = obj.rot
        var width = def.width
        var length = def.length
        val flag = def.clipFlag
        val blockBits = 4

        if (type == 10 || type == 11 || type == 22) {
            val blockedDirections = (DataConstants.BIT_MASK[blockBits] and (flag shl rot)) + (flag shr (blockBits - rot))

            if (rot == 1 || rot == 3) {
                width = def.length
                length = def.width
            }

            println("blocked=$blockedDirections, flag=$flag, rot=$rot, type=$type width=$width, length=$length, tile=${obj.tile}, player=${p.tile}")

            if (blockedDirections == 0) {
                if (width == 1 && length == 1) {
                    for (x in -1..1) {
                        for (z in -1..1) {
                            if (x == -1 && z == -1 || x == 1 && z == 1 || x == 1 && z == -1 || x == -1 && z == 1) {
                                continue
                            }
                            val tile = obj.tile.transform(x, z)
                            if (p.tile.sameAs(tile)) {
                                return true
                            }
                        }
                    }
                } else {
                    when (rot) {
                        1 -> {
                            for (x in 0 until width) {
                                for (z in -1 until length) {
                                    val tile = obj.tile.transform(x, z)
                                    if (p.tile.sameAs(tile)) {
                                        return true
                                    }
                                }
                            }
                        }
                        3 -> {
                            for (x in 0 until width) {
                                for (z in 0..length) {
                                    val tile = obj.tile.transform(x, z)
                                    if (p.tile.sameAs(tile)) {
                                        return true
                                    }
                                }
                            }
                        }
                        else -> {
                            for (x in 0..width) {
                                for (z in 0..length) {
                                    val tile = obj.tile.transform(x, z)
                                    if (p.tile.sameAs(tile)) {
                                        return true
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                //TODO: find flag for east only

                /**
                 * Can only interact from West side.
                 */
                if ((blockedDirections and 0x7) == 0x7) {
                    for (z in 0 until length) {
                        val tile = obj.tile.transform(-width, z)
                        if (p.tile.sameAs(tile)) {
                            return true
                        }
                    }
                }

                /**
                 * Can only interact from North side.
                 */
                if ((blockedDirections and 0xe) == 0xe) {
                    // can interact from north
                    for (x in 0 until width) {
                        val tile = obj.tile.transform(x, length)
                        if (p.tile.sameAs(tile)) {
                            return true
                        }
                    }
                }

                /**
                 * Can only interact from South side.
                 */
                if ((blockedDirections and 0xb) == 0xb) {
                    for (x in 0 until width) {
                        val tile = obj.tile.transform(x, -length)
                        if (p.tile.sameAs(tile)) {
                            return true
                        }
                    }
                }
            }
        } else {
            if (width == 1 && length == 1) {
                return when (rot) {
                    0, 2 -> arrayOf(obj.tile.transform(-1, 0), obj.tile.transform(0, 0)).any { p.tile.sameAs(it) }
                    1, 3 -> arrayOf(obj.tile.transform(0, 1), obj.tile.transform(0, 0)).any { p.tile.sameAs(it) }
                    else -> false
                }
            }
        }
        return false
    }
}