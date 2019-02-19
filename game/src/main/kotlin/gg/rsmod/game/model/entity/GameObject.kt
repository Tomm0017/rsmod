package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.attr.AttributeSystem
import gg.rsmod.game.model.timer.TimerSystem

/**
 * A [GameObject] is any type of map object that can occupy a tile.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class GameObject : Entity {

    /**
     * The object id.
     */
    val id: Int

    /**
     * A bit-packed byte that holds the object "type" and "rotation".
     */
    val settings: Byte

    /**
     * @see [AttributeSystem]
     */
    val attr = AttributeSystem()

    /**
     * @see [TimerSystem]
     */
    val timers = TimerSystem()

    val type: Int
        get() = settings.toInt() shr 2

    val rot: Int
        get() = settings.toInt() and 3

    private constructor(id: Int, settings: Int, tile: Tile) {
        this.id = id
        this.settings = settings.toByte()
        this.tile = tile
    }

    constructor(id: Int, type: Int, rot: Int, tile: Tile) : this(id, (type shl 2) or rot, tile)

    fun getDef(definitions: DefinitionSet): ObjectDef = definitions.get(ObjectDef::class.java, id)

    fun isSpawned(world: World): Boolean = world.isSpawned(this)

    /**
     * Npcs can change their appearance for each player depending on their
     * [NpcDef.transforms] and [NpcDef.varp]/[NpcDef.varbit]. This method will
     * get the "visually correct" npc id for this npc from [player]'s view point.
     */
    fun getTransform(player: Player): Int {
        val world = player.world
        val def = getDef(world.definitions)

        if (def.varbit != -1) {
            val varbitDef = world.definitions.get(VarbitDef::class.java, def.varbit)
            val state = player.varps.getBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit)
            return def.transforms!![state]
        }

        if (def.varp != -1) {
            val state = player.varps.getState(def.varp)
            return def.transforms!![state]
        }

        return id
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("type", type).add("rot", rot).add("tile", tile.toString()).toString()
}