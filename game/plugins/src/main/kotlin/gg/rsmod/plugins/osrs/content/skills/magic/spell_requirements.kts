import gg.rsmod.plugins.osrs.api.helper.getInteractingNpc
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.osrs.content.skills.magic.SpellRequirements

val STANDARD_SPELL_DATA_ENUM = 1982
val ANCIENT_SPELL_DATA_ENUM = 1983

SpellRequirements.loadSpellRequirements(world, STANDARD_SPELL_DATA_ENUM)
SpellRequirements.loadSpellRequirements(world, ANCIENT_SPELL_DATA_ENUM)

SpellRequirements.requirements.filter { it.value.combat }.forEach { entry ->
    val requirement = entry.value
    r.bindSpellOnNpc(requirement.parent, requirement.child) {
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