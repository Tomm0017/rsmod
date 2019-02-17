package gg.rsmod.plugins.osrs.content.mechanics.run

import gg.rsmod.plugins.osrs.api.ext.player

on_login {
    it.player().timers[RunEnergy.RUN_DRAIN] = 1
}

on_timer(RunEnergy.RUN_DRAIN) {
    val p = it.player()
    p.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(p)
}

/**
 * Button by minimap.
 */
on_button(interfaceId = 160, component = 22) {
    RunEnergy.toggle(it.player())
}

/**
 * Settings button.
 */
on_button(interfaceId = 261, component = 95) {
    RunEnergy.toggle(it.player())
}