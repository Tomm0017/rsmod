
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setVarp
import gg.rsmod.plugins.osrs.api.helper.toggleVarbit
import gg.rsmod.plugins.osrs.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */

r.bindButton(593, 3) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}

r.bindButton(593, 7) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 1)
}

r.bindButton(593, 11) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 2)
}

r.bindButton(593, 15) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 3)
}

r.bindButton(593, 27) {
    val p = it.player()
    p.toggleVarbit(AttackTab.AUTO_RETALIATE_VARP)
}

r.bindEquipSlot(EquipmentType.WEAPON.id) {
    it.player().setVarp(AttackTab.ENABLED_VARP, 0)
}