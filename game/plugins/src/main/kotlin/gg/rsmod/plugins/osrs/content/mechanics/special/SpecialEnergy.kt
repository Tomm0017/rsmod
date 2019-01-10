package gg.rsmod.plugins.osrs.content.mechanics.special

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.setVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialEnergy {

    const val ENERGY_VARP = 300
    const val ENABLED_VARP = 301

    fun setEnergy(p: Player, amount: Int) {
        check(amount in 0..100)
        p.setVarp(ENERGY_VARP, amount * 10)
    }
}