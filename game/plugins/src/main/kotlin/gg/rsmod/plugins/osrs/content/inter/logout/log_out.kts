import gg.rsmod.game.message.impl.LogoutFullMessage
import gg.rsmod.game.model.ACTIVE_COMBAT_TIMER
import gg.rsmod.plugins.osrs.api.ext.player

/**
 * Logout button.
 */
onButton(parent = 182, child = 8) {
    val p = it.player()
    if (!p.timers.has(ACTIVE_COMBAT_TIMER)) {
        p.requestLogout()
        p.write(LogoutFullMessage())
        p.channelClose()
    } else {
        p.message("You can't log out until 10 seconds after the end of combat.")
    }
}