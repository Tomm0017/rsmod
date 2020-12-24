package gg.rsmod.plugins.content.inter.welcome

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.interf.DisplayMode
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.api.getDisplayComponentId

object WelcomeScreen {
    const val WELCOME_SCREEN_ID = 378

    fun openChristmasWelcome(player: Player) {
        player.setVarp(261, 666) // last logged on 666 minutes ago
        player.setVarp(1780, 400) // 400 days of membership left

        player.openOverlayInterface(DisplayMode.FULLSCREEN)
        player.openInterface(getDisplayComponentId(DisplayMode.FULLSCREEN), 29, WELCOME_SCREEN_ID)

        player.setComponentText(WELCOME_SCREEN_ID, 7, "<col=2f2fff>Happy Christmas!</col><br>" +
                "<col=003900>Rev193</col> thanks to the fine folks in the <col=ffff00>RsMod</col> Discord<br>" +
                "drop in <col=9f0000>and thank</col> people like <col=ffffff>Tomm, Kris, & Bart</col>.")

        player.setComponentHidden(WELCOME_SCREEN_ID, 51, false)

        player.runClientScript(1080, "https://discord.gg/UznZnZR")
        player.runClientScript(828, 1) // members for Christmas
    }
}