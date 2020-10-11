package gg.rsmod.game.model.varp.delegate.impl

import gg.rsmod.game.model.varp.delegate.VarpDelegate

class IntVarp(varpId: Int): VarpDelegate<Int>(varpId) {
    override fun translateIn(inValue: Int): Int = inValue
    override fun translateOut(outValue: Int): Int = outValue
}
