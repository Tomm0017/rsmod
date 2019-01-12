
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.api.helper.setVarp
import gg.rsmod.plugins.osrs.api.helper.toggleVarp
import gg.rsmod.plugins.osrs.content.inter.attack.AttackTab

/**
 * Attack style buttons
 */
r.bindButton(parent = 593, child = 3) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}

r.bindButton(parent = 593, child = 7) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 1)
}

r.bindButton(parent = 593, child = 11) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 2)
}

r.bindButton(parent = 593, child = 15) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 3)
}

/**
 * Toggle auto-retaliate button.
 */
r.bindButton(parent = 593, child = 29) {
    val p = it.player()
    p.toggleVarp(AttackTab.AUTO_RETALIATE_VARP)
}

/**
 * Toggle special attack.
 */
r.bindButton(parent = 593, child = 35) {
    val p = it.player()
    p.toggleVarp(AttackTab.SPECIAL_ATTACK_VARP)
}

/**
 * Disable special attack when switching weapons.
 */
r.bindEquipSlot(EquipmentType.WEAPON.id) {
    it.player().setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}