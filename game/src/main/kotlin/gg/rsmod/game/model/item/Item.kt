package gg.rsmod.game.model.item

import com.google.common.base.MoreObjects
import com.google.common.collect.Maps
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Item(val id: Int, var amount: Int = 1) {

    constructor(other: Item) : this(other.id, other.amount)

    private var attr: EnumMap<ItemAttribute, Int>? = null

    fun toNote(definitions: DefinitionSet): Item {
        val def = definitions[ItemDef::class.java][id]
        return if (def.noteTemplateId == 0 && def.noteTemplateId > 0) Item(def.noteLinkId, amount) else Item(this)
    }

    fun toUnnote(definitions: DefinitionSet): Item {
        val def = definitions[ItemDef::class.java][id]
        return if (def.noteTemplateId > 0) Item(def.noteLinkId, amount) else Item(this)
    }

    fun hasAnyAttr(): Boolean = attr != null && attr!!.isNotEmpty()

    fun getAttrNullable(attrib: ItemAttribute): Int? = attr?.get(attrib)

    fun getAttr(attrib: ItemAttribute): Int {
        constructAttrIfNeeded()
        return attr!![attrib] ?: -1
    }

    private fun constructAttrIfNeeded() {
        attr = Maps.newEnumMap(ItemAttribute::class.java)
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("amount", amount).toString()
}