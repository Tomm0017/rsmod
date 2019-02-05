package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.model.combat.AttackStyle
import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Npc.prepareAtttack(combatClass: CombatClass, combatStyle: CombatStyle, attackStyle: AttackStyle) {
    this.combatClass = combatClass
    this.combatStyle = combatStyle
    this.attackStyle = attackStyle
}