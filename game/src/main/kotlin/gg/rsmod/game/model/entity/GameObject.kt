package gg.rsmod.game.model.entity

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ObjectDef
import gg.rsmod.game.model.AttributeSystem
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.TimerSystem

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

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("type", type).add("rot", rot).add("tile", tile.toString()).toString()
}