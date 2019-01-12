
import gg.rsmod.plugins.osrs.content.mechanics.run.RunEnergy
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setVarp
import gg.rsmod.plugins.osrs.api.helper.toggleVarp

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