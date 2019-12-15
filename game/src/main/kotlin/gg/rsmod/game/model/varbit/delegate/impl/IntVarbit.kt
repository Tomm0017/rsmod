package gg.rsmod.game.model.varbit.delegate.impl

import gg.rsmod.game.model.varbit.delegate.VarbitDelegate

class IntVarbit(varbitId: Int): VarbitDelegate<Int>(varbitId) {
    override fun translateIn(inValue: Int): Int = inValue
    override fun translateOut(outValue: Int): Int = outValue
}