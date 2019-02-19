package gg.rsmod.plugins.content.mechanics.skullremoval

import gg.rsmod.game.model.timer.SKULL_ICON_DURATION_TIMER
import gg.rsmod.plugins.api.SkullIcon
import gg.rsmod.plugins.api.ext.hasSkullIcon
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setSkullIcon

on_timer(SKULL_ICON_DURATION_TIMER) {
    val player = it.player()
    if (!player.hasSkullIcon(SkullIcon.NONE)) {
        player.setSkullIcon(SkullIcon.NONE)
    }
}