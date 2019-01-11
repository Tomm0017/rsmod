
import gg.rsmod.game.model.Privilege
import gg.rsmod.game.plugin.Plugin
import gg.rsmod.plugins.osrs.api.helper.doubleItemDialog
import gg.rsmod.plugins.osrs.api.helper.itemDialog
import gg.rsmod.plugins.osrs.api.helper.player

/**
 * @author Tom <rspsmods@gmail.com>
 */

r.bindLogin {
    val p = it.player()
    if (p.world.gameContext.initialLaunch) {
        p.world.gameContext.initialLaunch = false

        p.privilege = p.world.privileges.get(Privilege.OWNER_POWER)
                ?: p.world.privileges.get(Privilege.ADMIN_POWER)
                ?: p.world.privileges.get(Privilege.DEV_POWER)
                ?: Privilege.DEFAULT
        it.suspendable { dialog(it) }
    }
}

suspend fun dialog(it: Plugin) {
    val p = it.player()

    val api = p.world.server.getApiName()
    val site = p.world.server.getApiSite()

    p.graphic(id = 1388, height = 124)
    it.doubleItemDialog("<u=801700><col=801700>Welcome to $api</col></u><br><br>Welcome to your new server, ${p.username}!", item1 = 11863, item2 = 11847)
    p.graphic(id = 1388, height = 124)
    it.itemDialog("Be sure to go over the <col=801700>README</col> file on the project root directory for basic information on the programming aspect of <col=801700>$api</col>.", item = 757)
    it.itemDialog("You can visit the official website at <col=801700>$site</col> to make suggestions, purchase & sell plugins, or report bugs.", item = 13652)
}
