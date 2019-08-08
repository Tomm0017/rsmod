package gg.rsmod.plugins.content.mechanics.hitpoints

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object HitPoints {

    val RECOVERY = TimerKey()

    fun recovery(p: Player) {
        if (p.getCurrentHp() < p.getMaxHp()) {
            var recovery = (1)
            val max = p.getMaxHp()

            p.hitpoints = Math.min(max, p.getCurrentHp() + recovery)
            p.heal(recovery, p.getMaxHp())
        }
    }
}