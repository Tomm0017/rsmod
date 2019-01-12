
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.mechanics.run.RunEnergy

r.bindLogin {
    it.player().timers[RunEnergy.RUN_DRAIN] = 1
}

r.bindTimer(RunEnergy.RUN_DRAIN) {
    val p = it.player()
    p.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(p)
}

/**
 * Button by minimap.
 */
r.bindButton(parent = 160, child = 22) {
    RunEnergy.toggle(it.player())
}

/**
 * Settings button.
 */
r.bindButton(parent = 261, child = 95) {
    RunEnergy.toggle(it.player())
}