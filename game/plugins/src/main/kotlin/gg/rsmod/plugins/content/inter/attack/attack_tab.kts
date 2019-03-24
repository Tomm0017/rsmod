package gg.rsmod.plugins.content.inter.attack

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR

/**
 * First bait-in logic (when accounts have just been made).
 */
on_login {
    val player = player
    if (player.attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
        AttackTab.setEnergy(player, 100)
    }
}

/**
 * Attack style buttons
 */
on_button(interfaceId = 593, component = 3) {
    val p = player
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}

on_button(interfaceId = 593, component = 7) {
    val p = player
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 1)
}

on_button(interfaceId = 593, component = 11) {
    val p = player
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 2)
}

on_button(interfaceId = 593, component = 15) {
    val p = player
    p.setVarp(AttackTab.ATTACK_STYLE_VARP, 3)
}

/**
 * Toggle auto-retaliate button.
 */
on_button(interfaceId = 593, component = 29) {
    val p = player
    p.toggleVarp(AttackTab.DISABLE_AUTO_RETALIATE_VARP)
}

/**
 * Toggle special attack.
 */
on_button(interfaceId = 593, component = 35) {
    val p = player
    p.toggleVarp(AttackTab.SPECIAL_ATTACK_VARP)
}

/**
 * Disable special attack when switching weapons.
 */
on_equip_to_slot(EquipmentType.WEAPON.id) {
    player.setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}

/**
 * Disable special attack on bait-out.
 */
on_logout {
    player.setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}