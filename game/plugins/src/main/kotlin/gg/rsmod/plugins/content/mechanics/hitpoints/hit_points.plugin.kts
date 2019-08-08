package gg.rsmod.plugins.content.mechanics.hitpoints

on_login {
    player.timers[HitPoints.RECOVERY] = 100
}

on_timer(HitPoints.RECOVERY) {
    player.timers[HitPoints.RECOVERY] = 100
    HitPoints.recovery(player)
}