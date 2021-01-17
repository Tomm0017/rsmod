package gg.rsmod.plugins.content.inter.welcome

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.api.getDisplayComponentId

object WelcomeScreen {
    const val WELCOME_SCREEN_INTERFACE_ID = 378

    fun openChristmasWelcome(player: Player, minSinceLogin: Int) {
        player.setVarp(261, minSinceLogin)
        player.setVarp(1780, player.membersDaysLeft())

        val fullScreenRoot = getDisplayComponentId(DisplayMode.FULLSCREEN)

        player.openOverlayInterface(DisplayMode.FULLSCREEN)
        player.openInterface(fullScreenRoot, 29, WELCOME_SCREEN_INTERFACE_ID)
        player.openInterface(fullScreenRoot, 2, 651)

        player.setComponentText(WELCOME_SCREEN_INTERFACE_ID, 73, "You do not have a Bank PIN.<br>Please visit a bank if you would like one.")

        player.setComponentText(WELCOME_SCREEN_INTERFACE_ID, 7, "<col=2f2fff>Happy Christmas!</col><br>" +
                "<col=003900>Rev193</col> thanks to the fine folks in the <col=ffff00>RsMod</col> Discord<br>" +
                "drop in <col=9f0000>and thank</col> people like <col=ffffff>Tomm, Kris, & Bart</col>.")

        player.setComponentHidden(WELCOME_SCREEN_INTERFACE_ID, 51, false)

        player.runClientScript(1080, "https://discord.gg/UznZnZR")
        player.runClientScript(828, 1)
    }
}