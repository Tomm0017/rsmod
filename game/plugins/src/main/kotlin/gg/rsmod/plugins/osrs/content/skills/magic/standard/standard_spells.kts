import gg.rsmod.plugins.osrs.api.helper.getInteractingNpc
import gg.rsmod.plugins.osrs.api.helper.player
import gg.rsmod.plugins.osrs.content.combat.Combat
import gg.rsmod.plugins.osrs.content.combat.strategy.magic.CombatSpell
import gg.rsmod.plugins.osrs.content.skills.magic.SpellRequirements


SpellRequirements.requirements.filter { it.value.combat }.forEach { entry ->
    val requirement = entry.value
    r.bindSpellOnNpc(requirement.parent, requirement.child) {
        val player = it.player()
        val npc = it.getInteractingNpc()
        val combatSpell = CombatSpell.values.first { spell -> spell.id == requirement.spellId }
        player.attr[Combat.CASTING_SPELL] = combatSpell
        player.attack(npc)
    }
}