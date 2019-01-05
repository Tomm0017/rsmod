package gg.rsmod.game.model.item

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Item(val id: Int, var amount: Int = 1) {

    constructor(other: Item) : this(other.id, other.amount) {
        copyAttr(other)
    }

    private var attr: HashMap<ItemAttribute, Int>? = null

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

    fun putAttr(attrib: ItemAttribute, value: Int): Item {
        constructAttrIfNeeded()
        attr!![attrib] = value
        return this
    }

    fun copyAttr(other: Item) {
        if (other.hasAnyAttr()) {
            constructAttrIfNeeded()
            attr!!.putAll(other.attr!!)
        }
    }

    private fun constructAttrIfNeeded() {
        if (attr == null) {
            attr = hashMapOf()
        }
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("amount", amount).toString()
}