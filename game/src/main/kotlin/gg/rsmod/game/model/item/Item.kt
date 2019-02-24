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

    constructor(other: Item, amount: Int) : this(other.id, amount) {
        copyAttr(other)
    }

    private var attr: HashMap<ItemAttribute, Int>? = null

    /**
     * Returns a <strong>new</strong> [Item] with the noted link as the item id.
     * If this item does not have a noted link item id, it will return a new [Item]
     * with the same [Item.id].
     */
    fun toNoted(definitions: DefinitionSet): Item {
        val def = definitions.get(ItemDef::class.java, id)
        return if (def.noteTemplateId == 0 && def.noteLinkId > 0) Item(def.noteLinkId, amount).copyAttr(this) else Item(this).copyAttr(this)
    }

    /**
     * Returns a <strong>new</strong> [Item] with the unnoted link as the item id.
     * If this item does not have a unnoted link item id, it will return a new [Item]
     * with the same [Item.id].
     */
    fun toUnnoted(definitions: DefinitionSet): Item {
        val def = definitions.get(ItemDef::class.java, id)
        return if (def.noteTemplateId > 0) Item(def.noteLinkId, amount).copyAttr(this) else Item(this).copyAttr(this)
    }

    fun getDef(definitions: DefinitionSet) = definitions.get(ItemDef::class.java, id)

    /**
     * Returns true if [attr] contains any value.
     */
    fun hasAnyAttr(): Boolean = attr != null && attr!!.isNotEmpty()

    fun getAttr(attrib: ItemAttribute): Int? {
        constructAttrIfNeeded()
        return attr!![attrib] ?: -1
    }

    fun putAttr(attrib: ItemAttribute, value: Int): Item {
        constructAttrIfNeeded()
        attr!![attrib] = value
        return this
    }

    /**
     * Copies the [Item.attr] map from [other] to this.
     */
    fun copyAttr(other: Item): Item {
        if (other.hasAnyAttr()) {
            constructAttrIfNeeded()
            attr!!.putAll(other.attr!!)
        }
        return this
    }

    private fun constructAttrIfNeeded() {
        if (attr == null) {
            attr = hashMapOf()
        }
    }

    override fun toString(): String = MoreObjects.toStringHelper(this).add("id", id).add("amount", amount).toString()
}