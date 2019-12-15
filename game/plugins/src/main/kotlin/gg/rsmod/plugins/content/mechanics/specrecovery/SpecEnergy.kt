package gg.rsmod.plugins.content.mechanics.specrecovery

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.plugins.api.ext.heal
import gg.rsmod.plugins.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecEnergy {

    val RECOVERY = TimerKey()

    fun recovery(p: Player) {
        if (AttackTab.getEnergy(p) < 100) {
            var recovery = 10
            val max = 100

            val specenergy = (AttackTab.getEnergy(p) + recovery)
            AttackTab.setEnergy(p, specenergy)
        }
    }
}