package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Player
import gg.rsmod.util.BitManipulation

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BitStorage private constructor(val key: AttributeKey<Int>?, private val persistentKey: String?) {

    /**
     * Use when this [StorageBits] does not need to persist (save) on log-out.
     */
    constructor(key: AttributeKey<Int>) : this(key, null)

    /**
     * Use when this [BitStorage] should persist (save) on log-out.
     */
    constructor(persistenceKey: String) : this(null, persistenceKey)

    fun get(p: Player, bits: StorageBits): Int = BitManipulation.getBit(packed = get(p), startBit = bits.startBit, endBit = bits.endBit)

    fun set(p: Player, bits: StorageBits, value: Int) {
        set(p, BitManipulation.setBit(packed = get(p), startBit = bits.startBit, endBit = bits.endBit, value = value))
    }

    private fun get(p: Player): Int = if (persistentKey != null) (p.getPersistentAttr(persistentKey) ?: 0) else (p.attr[key!!] ?: 0)

    private fun set(p: Player, packed: Int) {
        if (persistentKey != null) {
            p.putPersistentAttr(persistentKey, packed)
        } else {
            p.attr.put(key!!, packed)
        }
    }
}