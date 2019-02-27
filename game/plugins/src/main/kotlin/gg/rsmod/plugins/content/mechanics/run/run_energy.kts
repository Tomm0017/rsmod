package gg.rsmod.plugins.content.mechanics.run

import gg.rsmod.plugins.api.ext.player

on_login {
    player.timers[RunEnergy.RUN_DRAIN] = 1
}

on_timer(RunEnergy.RUN_DRAIN) {
    val p = player
    p.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(p)
}

/**
 * Button by minimap.
 */
on_button(interfaceId = 160, component = 22) {
    RunEnergy.toggle(player)
}

/**
 * Settings button.
 */
on_button(interfaceId = 261, component = 95) {
    RunEnergy.toggle(player)
}