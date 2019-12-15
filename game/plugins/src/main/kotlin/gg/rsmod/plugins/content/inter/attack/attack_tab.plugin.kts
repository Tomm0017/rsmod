package gg.rsmod.plugins.content.inter.attack

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.combat.specialattack.ExecutionType
import gg.rsmod.plugins.content.combat.specialattack.SpecialAttacks
import gg.rsmod.plugins.content.inter.autocasts.AutoCasting

/**
 * First log-in logic (when accounts have just been made).
 */
on_login {
    if (player.attr.getOrDefault(NEW_ACCOUNT_ATTR, false)) {
        AttackTab.setEnergy(player, 100)
    }
}

/**
 * Attack style buttons
 */
on_button(interfaceId = 593, component = 4) {
    AutoCasting.disableSelectedAutoCastSpell(player)
    player.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}

on_button(interfaceId = 593, component = 8) {
    AutoCasting.disableSelectedAutoCastSpell(player)
    player.setVarp(AttackTab.ATTACK_STYLE_VARP, 1)
}

on_button(interfaceId = 593, component = 12) {
    AutoCasting.disableSelectedAutoCastSpell(player)
    player.setVarp(AttackTab.ATTACK_STYLE_VARP, 2)
}

on_button(interfaceId = 593, component = 16) {
    AutoCasting.disableSelectedAutoCastSpell(player)
    player.setVarp(AttackTab.ATTACK_STYLE_VARP, 3)
}

/**
 * Toggle Magic-Defensive attackstyle
 */
on_button(interfaceId = 593, component = 21) {
    player.setVarbit(Combat.DEFENSIVE_MAGIC_CAST_VARBIT, 1)
    AutoCasting.openAutoCastSpellsList(player)
}
/**
 * Toggle Magic-Aggressive attackstyle
 */
on_button(interfaceId = 593, component = 26) {
    player.setVarbit(Combat.DEFENSIVE_MAGIC_CAST_VARBIT, 0)
    AutoCasting.openAutoCastSpellsList(player)
}

/**
 * Toggle auto-retaliate button.
 */
on_button(interfaceId = 593, component = 30) {
    player.toggleVarp(AttackTab.DISABLE_AUTO_RETALIATE_VARP)
}

/**
 * Toggle special attack.
 */
on_button(interfaceId = 593, component = 36) {
    player.toggleVarp(AttackTab.SPECIAL_ATTACK_VARP)
    if(AttackTab.isSpecialEnabled(player)) {
        SpecialAttacks.execute(player,null,  world, ExecutionType.EXECUTE_ON_ACTIVATION)
    }
}

/**
 * Toggle special attack.
 */
on_button(interfaceId = 160, component = 30) {
    player.toggleVarp(AttackTab.SPECIAL_ATTACK_VARP)
    if(AttackTab.isSpecialEnabled(player)) {
        SpecialAttacks.execute(player,null,  world, ExecutionType.EXECUTE_ON_ACTIVATION)
    }
}

/**
 * Disable special attack when switching weapons.
 */
on_equip_to_slot(EquipmentType.WEAPON.id) {
    player.setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}

/**
 * Disable special attack on log-out.
 */
on_logout {
    player.setVarp(AttackTab.SPECIAL_ATTACK_VARP, 0)
}