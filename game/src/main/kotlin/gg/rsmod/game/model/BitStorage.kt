package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Player
import gg.rsmod.util.BitManipulation

/**
 * @author Tom <rspsmods@gmail.com>
 */
class BitStorage(val key: AttributeKey<Int>) {

    /**
     * Use when this [BitStorage] should persist (save) on log-out.
     */
    constructor(persistenceKey: String) : this(AttributeKey<Int>(persistenceKey))

    fun get(p: Player, bits: StorageBits): Int = BitManipulation.getBit(packed = get(p), startBit = bits.startBit, endBit = bits.endBit)

    fun set(p: Player, bits: StorageBits, value: Int) {
        set(p, BitManipulation.setBit(packed = get(p), startBit = bits.startBit, endBit = bits.endBit, value = value))
    }

    private fun get(p: Player): Int = p.attr[key] ?: 0

    private fun set(p: Player, packed: Int) {
        p.attr.put(key, packed)
    }
}