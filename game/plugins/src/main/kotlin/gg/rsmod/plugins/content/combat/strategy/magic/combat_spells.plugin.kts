package gg.rsmod.plugins.content.combat.strategy.magic

import gg.rsmod.plugins.api.Spellbook
import gg.rsmod.plugins.content.combat.Combat
import gg.rsmod.plugins.content.magic.MagicSpells
import gg.rsmod.plugins.content.magic.SpellMetadata

if (!MagicSpells.isLoaded()) {
    MagicSpells.loadSpellRequirements(world)
}

MagicSpells.getCombatSpells().forEach { entry ->
    val requirement = entry.value
    val standard = requirement.spellbook == Spellbook.NORMAL.id
    val ancients = requirement.spellbook == Spellbook.ANCIENTS.id

    if (standard || ancients) {
        on_spell_on_npc(requirement.interfaceId, requirement.component) {
            castCombatSpellOnPawn(player, player.getInteractingNpc(), requirement)
        }

        on_spell_on_player(requirement.interfaceId, requirement.component) {
            castCombatSpellOnPawn(player, player.getInteractingPlayer(), requirement)
        }
    }
}

fun castCombatSpellOnPawn(player: Player, pawn: Pawn, spellMetadata: SpellMetadata) {
    val combatSpell = CombatSpell.values.firstOrNull { spell -> spell.id == spellMetadata.paramItem }
    if (combatSpell != null) {
        player.attr[Combat.CASTING_SPELL] = combatSpell
        player.attack(pawn)
    } else {
        /*
         * The spell is not defined in [CombatSpell].
         */
        if (world.devContext.debugMagicSpells) {
            player.message("Undefined combat spell: [spellId=${spellMetadata.paramItem}, name=${spellMetadata.name}]")
        }
    }
}