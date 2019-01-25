
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.mechanics.run.RunEnergy

onLogin {
    it.player().timers[RunEnergy.RUN_DRAIN] = 1
}

onTimer(RunEnergy.RUN_DRAIN) {
    val p = it.player()
    p.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(p)
}

/**
 * Button by minimap.
 */
onButton(parent = 160, child = 22) {
    RunEnergy.toggle(it.player())
}

/**
 * Settings button.
 */
onButton(parent = 261, child = 95) {
    RunEnergy.toggle(it.player())
}