package gg.rsmod.plugins.content.mechanics.spells

import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.combat.strategy.magic.CombatSpell

val SPELLBOOK_POINTER_ENUM = 1981

SpellRequirements.loadSpellRequirements(world, SPELLBOOK_POINTER_ENUM)

SpellRequirements.requirements.filter { it.value.combat }.forEach { entry ->
    val requirement = entry.value
    on_spell_on_npc(requirement.parent, requirement.child) {
        val player = player
        val npc = getInteractingNpc()
        val combatSpell = CombatSpell.values.firstOrNull { spell -> spell.id == requirement.spellId }
        if (combatSpell != null) {
            player.attr[Combat.CASTING_SPELL] = combatSpell
            player.attack(npc)
        } else {
            /**
             * The spell is not defined in [CombatSpell].
             */
            if (player.world.devContext.debugMagicSpells) {
                player.message("Undefined combat spell: [spellId=${requirement.spellId}, name=${requirement.name}]")
            }
        }
    }
}