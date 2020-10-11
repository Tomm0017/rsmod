package gg.rsmod.game.model.varbit.delegate.impl

import gg.rsmod.game.model.varbit.delegate.VarbitDelegate

class BoolVarbit(varbitId: Int): VarbitDelegate<Boolean>(varbitId) {
    override fun translateIn(inValue: Boolean): Int = if (inValue) 1 else 0
    override fun translateOut(outValue: Int): Boolean = outValue != 0
}
