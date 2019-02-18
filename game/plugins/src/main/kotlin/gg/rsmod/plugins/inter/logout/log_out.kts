package gg.rsmod.plugins.content.inter.logout

import gg.rsmod.game.message.impl.LogoutFullMessage
import gg.rsmod.game.model.timer.ACTIVE_COMBAT_TIMER
import gg.rsmod.plugins.api.ext.player

/**
 * Logout button.
 */
on_button(interfaceId = 182, component = 8) {
    val p = it.player()
    if (!p.timers.has(ACTIVE_COMBAT_TIMER)) {
        p.requestLogout()
        p.write(LogoutFullMessage())
        p.channelClose()
    } else {
        p.message("You can't log out until 10 seconds after the end of combat.")
    }
}