package gg.rsmod.plugins.content.inter.attack

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setVarp
import gg.rsmod.plugins.api.ext.toggleVarp

/**
 * First log-in logic (when accounts have just been made).
 */
on_login {
    val player = it.player()
    if (player.attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
        AttackTab.setEnergy(player, 100)
    }
}

/**
 * Attack style buttons
 */
on_button(interfaceId = 593, component = 3) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}

on_button(interfaceId = 593, component = 7) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 1)
}

on_button(interfaceId = 593, component = 11) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 2)
}

on_button(interfaceId = 593, component = 15) {
    val p = it.player()
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 3)
}

/**
 * Toggle auto-retaliate button.
 */
on_button(interfaceId = 593, component = 29) {
    val p = it.player()
    p.toggleVarp(AttackTab.AUTO_RETALIATE_VARP)
}

/**
 * Toggle special attack.
 */
on_button(interfaceId = 593, component = 35) {
    val p = it.player()
    p.toggleVarp(AttackTab.SPECIAL_ATTACK_VARP)
}

/**
 * Disable special attack when switching weapons.
 */
on_equip_to_slot(EquipmentType.WEAPON.id) {
    it.player().setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}

/**
 * Disable special attack on log-out.
 */
on_logout {
    it.player().setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}