package gg.rsmod.plugins.content.inter.autocasts

import gg.rsmod.plugins.api.ext.getInteractingSlot
import gg.rsmod.plugins.api.ext.getVarp
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.api.ext.setVarp
import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.content.inter.attack.AttackTab
import gg.rsmod.plugins.content.magic.MagicSpells

/**
 * Loops Combat Spells and assigns autcast spell for each button
 */
on_button(interfaceId = AutoCasting.INTERFACE_ID,component = 1) {
        val slot = player.getInteractingSlot()
        CombatSpell.values.forEach { spell ->
             when (slot) {
                 0 -> {//Close button
                     if(player.getVarp(AttackTab.ATTACK_STYLE_VARP) == 4) {
                         player.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
                     }
                     player.attr.remove(Combat.CASTING_SPELL)
                     AutoCasting.disableSelectedAutoCastSpell(player)
                 }
                spell.autoCastId -> {//spell  button
                    val requirements = MagicSpells.getMetadata(spell.id)
                    if (requirements != null && !MagicSpells.canCast(player, requirements.lvl, requirements.items, requirements.spellbook)) {
                        if(player.getVarp(AttackTab.ATTACK_STYLE_VARP) == 4) {
                            player.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
                        }
                        player.attr.remove(Combat.CASTING_SPELL)
                        AutoCasting.disableSelectedAutoCastSpell(player)
                    } else {
                        player.setVarp(AttackTab.ATTACK_STYLE_VARP, 4)
                        player.attr[Combat.CASTING_SPELL] = spell
                        player.setVarbit(Combat.SELECTED_AUTOCAST_VARBIT, spell.autoCastId)
                    }
                }
            }
            player.openInterface(593, InterfaceDestination.ATTACK)
        }
    }
/**
 * Setup new spells or disables list in auto cast upon equipping a new weapon.
 */
on_equip_to_slot(EquipmentType.WEAPON.id) {
    player.openInterface(593, InterfaceDestination.ATTACK)
    AutoCasting.disableSelectedAutoCastSpell(player)
    player.setVarp(AutoCasting.AUTO_CAST_SPELL_LIST_VARP, 0)//TODO: Check staff id and set the right autocastspell id to it.
    player.setVarp(AttackTab.ATTACK_STYLE_VARP, 0)
}