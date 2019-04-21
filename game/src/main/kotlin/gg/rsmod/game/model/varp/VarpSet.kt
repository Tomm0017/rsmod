package gg.rsmod.game.model.varp

import gg.rsmod.util.BitManipulation
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet

/**
 * Holds a set of varps.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class VarpSet(val maxVarps: Int) {

    private val varps = mutableListOf<Varp>().apply {
        for (i in 0 until maxVarps) {
            add(Varp(id = i, state = 0))
        }
    }

    /**
     * A collection of dirty varps which will be sent to the client on the next cycle.
     * This collection should be used only if the revision you are trying to
     * support uses them on the client.
     */
    private val dirty = ShortOpenHashSet(maxVarps)

    operator fun get(id: Int): Varp = varps[id]

    fun getState(id: Int): Int = varps[id].state

    fun setState(id: Int, state: Int): VarpSet {
        varps[id].state = state
        dirty.add(id.toShort())
        return this
    }

    /**
     * Get the bits ranging from [startBit] to [endBit] in the [Varp] associated
     * with [id].
     *
     * @param id
     * The [Varp] id.
     *
     * @param startBit
     * The start of the bits to get the value from.
     *
     * @param endBit
     * The end of the bits to get the value from.
     */
    fun getBit(id: Int, startBit: Int, endBit: Int): Int = BitManipulation.getBit(getState(id), startBit, endBit)

    /**
     * Set the bits ranging from [startBit] to [endBit] to equal [value].
     * This can help store up to [32] possible values per [Varp], due to
     * [Varp.state] being a 32-bit integer.
     *
     * @param id
     * The id of the [Varp].
     *
     * @param startBit
     * The start of the bits to occupy with value.
     *
     * @param endBit
     * The end of the bits that can be occupied by value.
     *
     * @param value
     * The value that will be stored from [startBit] to [endBit].
     *
     */
    fun setBit(id: Int, startBit: Int, endBit: Int, value: Int): VarpSet {
        return setState(id, BitManipulation.setBit(getState(id), startBit, endBit, value))
    }

    fun isDirty(id: Int): Boolean {
        return dirty.contains(id.toShort())
    }

    fun clean() {
        dirty.clear()
    }

    fun getAll(): List<Varp> = varps
}