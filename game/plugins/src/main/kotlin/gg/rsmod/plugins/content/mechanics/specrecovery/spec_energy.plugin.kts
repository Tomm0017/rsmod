package gg.rsmod.plugins.content.mechanics.specrecovery

import gg.rsmod.plugins.content.inter.attack.AttackTab

on_login {
    player.timers[SpecEnergy.RECOVERY] = 100
}

on_timer(SpecEnergy.RECOVERY) {
    if (AttackTab.getEnergy(player) < 91) {
        SpecEnergy.recovery(player)
    } else {
        AttackTab.setEnergy(player, 100)
    }
    player.timers[SpecEnergy.RECOVERY] = 100
}