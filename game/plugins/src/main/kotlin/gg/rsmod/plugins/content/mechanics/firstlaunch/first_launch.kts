package gg.rsmod.plugins.content.mechanics.firstlaunch

import gg.rsmod.game.model.priv.Privilege
import gg.rsmod.plugins.content.inter.emotes.EmotesTab

on_login {
    if (world.gameContext.initialLaunch) {
        world.gameContext.initialLaunch = false

        player.privilege = world.privileges.get(Privilege.OWNER_POWER)
                ?: world.privileges.get(Privilege.ADMIN_POWER)
                ?: world.privileges.get(Privilege.DEV_POWER)
                ?: Privilege.DEFAULT

        EmotesTab.unlockAll(player)
        player.queue { dialog(this) }
    }
}

suspend fun dialog(it: QueueTask) {
    val api = world.server.getApiName()
    val site = world.server.getApiSite()

    it.player.graphic(id = 1388, height = 124)
    it.doubleItemMessageBox("<u=801700><col=801700>Welcome to $api</col></u><br><br>Welcome to your new server, ${it.player.username}!", item1 = 11863, item2 = 11847)
    it.player.graphic(id = 1388, height = 124)
    it.itemMessageBox("Be sure to go over the <col=801700>README</col> file on the project root directory for basic information on the programming aspect of <col=801700>$api</col>.", item = 757)
    it.itemMessageBox("You can visit the official website at <col=801700>$site</col> to make suggestions, purchase & sell plugins, or report bugs.", item = 13652)
}
