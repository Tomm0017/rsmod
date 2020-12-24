package gg.rsmod.plugins.content.inter.welcome

import gg.rsmod.plugins.content.inter.welcome.WelcomeScreen.WELCOME_SCREEN_ID
import gg.rsmod.plugins.content.Osrs_plugin.OSRSInterfaces.openDefaultInterfaces

on_button(WELCOME_SCREEN_ID, 78) {
    player.closeInterface(WELCOME_SCREEN_ID)
    openDefaultInterfaces(player)
}

on_button(WELCOME_SCREEN_ID, 86) {
    player.runClientScript(1081, "https://www.rune-server.ee/runescape-development/rs2-server/downloads/684206-181-rs-mod-release.html", 1, 1)

}

on_button(WELCOME_SCREEN_ID, 83) {
    player.runClientScript(1081, "https://github.com/bmyte/rsmod", 1, 1)

}