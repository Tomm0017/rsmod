import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.inter.kod.KeptOnDeath

r.bindButton(parent = 387, child = 21) {
    val p = it.player()
    if (!p.lock.canInterfaceInteract()) {
        return@bindButton
    }
    KeptOnDeath.open(it.player())
}