package gg.rsmod.game.model.varbit.delegate

import gg.rsmod.game.fs.def.VarbitDef
import gg.rsmod.game.model.entity.Player
import kotlin.reflect.KProperty

/**
 * Allows you to delegate a [Player] extension property to have quick simple and easy access to a varbit
 *
 * @property [varbitId] The id of the varbit you are writing to and reading from
 *
 * @author Curtis Woodard <Nbness2@gmail.com>
 */
abstract class VarbitDelegate<T>(val varbitId: Int) {

    private var _varbitDef: VarbitDef? = null

    /** @property [varbitDef] The [VarbitDef] used in [getVarbit] and [setVarbit] */
    val varbitDef: VarbitDef
        get() = _varbitDef ?: throw Exception("Varbit Def $varbitId not found")

    /**
     * Translates your input to [Int] to write to the varbit
     *
     * @param [inValue] The value you are translating to [Int]
     *
     * @return [Int]
     */
    abstract fun translateIn(inValue: T): Int

    /**
     * Translates the varbit [Int] to the output type [T]
     *
     * @param [outValue] The value you are translating to [T]
     *
     * @return [T]
     */
    abstract fun translateOut(outValue: Int): T

    operator fun getValue(player: Player, property: KProperty<*>): T {
        ensureVarbitDef(player)
        return translateOut(getVarbit(player))
    }

    operator fun setValue(player: Player, property: KProperty<*>, value: T) {
        ensureVarbitDef(player)
        setVarbit(player, translateIn(value))
    }

    private fun getVarbit(player: Player): Int {
        ensureVarbitDef(player)
        return player.varps.getBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit)
    }

    private fun setVarbit(player: Player, value: Int) {
        ensureVarbitDef(player)
        player.varps.setBit(varbitDef.varp, varbitDef.startBit, varbitDef.endBit, value)
    }

    /**
     * Ensures that [varbitDef] is set
     */
    private fun ensureVarbitDef(player: Player) {
        if (_varbitDef == null) {
            _varbitDef = player.world.definitions.get(VarbitDef::class.java, varbitId)
        }
    }
}