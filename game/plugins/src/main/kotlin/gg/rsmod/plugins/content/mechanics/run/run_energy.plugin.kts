package gg.rsmod.plugins.content.mechanics.run

on_login {
    player.timers[RunEnergy.RUN_DRAIN] = 1
}

on_timer(RunEnergy.RUN_DRAIN) {
    player.timers[RunEnergy.RUN_DRAIN] = 1
    RunEnergy.drain(player)
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