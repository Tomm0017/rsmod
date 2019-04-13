package gg.rsmod.plugins.content.combat.strategy.magic

import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.magic.MagicSpells

if (!MagicSpells.isLoaded()) {
    MagicSpells.loadSpellRequirements(world)
}

MagicSpells.getCombatSpells().forEach { entry ->
    val requirement = entry.value
    val standard = requirement.spellbook == Spellbook.STANDARD.id
    val ancients = requirement.spellbook == Spellbook.ANCIENT.id

    if (standard || ancients) {
        on_spell_on_npc(requirement.interfaceId, requirement.component) {
            val npc = player.getInteractingNpc()
            val combatSpell = CombatSpell.values.firstOrNull { spell -> spell.id == requirement.paramItem }
            if (combatSpell != null) {
                player.attr[Combat.CASTING_SPELL] = combatSpell
                player.attack(npc)
            } else {
                /*
                 * The spell is not defined in [CombatSpell].
                 */
                if (world.devContext.debugMagicSpells) {
                    player.message("Undefined combat spell: [spellId=${requirement.paramItem}, name=${requirement.name}]")
                }
            }
        }
    }
}