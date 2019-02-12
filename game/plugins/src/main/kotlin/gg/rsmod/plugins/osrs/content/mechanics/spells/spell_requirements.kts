import gg.rsmod.plugins.osrs.api.ext.getInteractingNpc
import gg.rsmod.plugins.osrs.api.ext.player
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.osrs.content.mechanics.spells.SpellRequirements

val SPELLBOOK_POINTER_ENUM = 1981

SpellRequirements.loadSpellRequirements(world, SPELLBOOK_POINTER_ENUM)

SpellRequirements.requirements.filter { it.value.combat }.forEach { entry ->
    val requirement = entry.value
    onSpellOnNpc(requirement.parent, requirement.child) {
        val player = it.player()
        val npc = it.getInteractingNpc()
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