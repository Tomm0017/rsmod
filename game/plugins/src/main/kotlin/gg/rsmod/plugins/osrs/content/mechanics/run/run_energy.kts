
import gg.rsmod.plugins.osrs.content.mechanics.run.RunEnergy
import gg.rsmod.plugins.player
import gg.rsmod.plugins.setVarp
import gg.rsmod.plugins.toggleVarp

/**
 * @author Tom <rspsmods@gmail.com>
 */

r.bindLogin {
    it.player().timers[RunEnergy.RUN_DRAIN] = 1
}

r.bindTimer(RunEnergy.RUN_DRAIN) {
    val p = it.player()
    p.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(p)
}

r.bindButton(160, 22) {
    if (it.player().runEnergy >= 1.0) {
        it.player().toggleVarp(RunEnergy.RUN_ENABLED_VARP)
    } else {
        it.player().setVarp(RunEnergy.RUN_ENABLED_VARP, 0)
        it.player().message("You don't have enough run energy left.")
    }
}