package gg.rsmod.game.model.item

import com.google.common.base.MoreObjects
import gg.rsmod.game.fs.DefinitionSet
import gg.rsmod.game.fs.def.ItemDef

/**
 * @author Tom <rspsmods@gmail.com>
 */
class Item(val id: Int, val amount: Int = 1) {

    constructor(other: Item) : this(other.id, other.amount)

    fun toNote(definitions: DefinitionSet): Item {
        val def = definitions[ItemDef::class.java][id]
        return if (def.noteTemplateId == 0 && def.noteTemplateId > 0) Item(def.noteLinkId, amount) else Item(this)
    }

    fun toUnnote(definitions: DefinitionSet): Item {
        val def = definitions[ItemDef::class.java][id]
        return if (def.noteTemplateId > 0) Item(def.noteLinkId, amount) else Item(this)
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("amount", amount).toString()
}