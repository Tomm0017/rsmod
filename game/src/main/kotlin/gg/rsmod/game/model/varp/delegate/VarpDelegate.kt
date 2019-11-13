package gg.rsmod.game.model.varp.delegate

import gg.rsmod.game.model.entity.Player
import kotlin.reflect.KProperty

/**
 * Allows you to delegate a [Player] extension property to have quick simple and easy access to a varp
 *
 * @property [varpId] The id of the varp you are writing to and reading from
 */
abstract class VarpDelegate<T>(val varpId: Int) {

    /**
     * Translates your input to [Int] to write to the varp
     *
     * @param [inValue] The value you are translating to [Int]
     */
    abstract fun translateIn(inValue: T): Int

    /**
     * Translates the varp [Int] to output type [T]
     *
     * @param [outValue] The value getting translated to [Int]
     */
    abstract fun translateOut(outValue: Int): T

    operator fun getValue(player: Player, property: KProperty<*>): T = translateOut(getVarp(player, varpId))

    operator fun setValue(player: Player, property: KProperty<*>, value: T) = setVarp(player, varpId, translateIn(value))

    companion object {
        fun getVarp(player: Player, varpId: Int): Int = player.varps.getState(varpId)

        fun setVarp(player: Player, varpId: Int, value: Int) = player.varps.setState(varpId, value)
    }
}
