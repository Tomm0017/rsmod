import gg.rsmod.game.message.impl.SendLogoutMessage
import gg.rsmod.plugins.osrs.api.helper.player

/**
 * Logout button.
 */
r.bindButton(parent = 182, child = 8) {
    it.player().requestLogout()
    it.player().write(SendLogoutMessage())
    it.player().channelClose()
}