package gg.rsmod.plugins.osrs.api.helper

import gg.rsmod.game.model.combat.CombatClass
import gg.rsmod.game.model.combat.CombatStyle
import gg.rsmod.game.model.entity.Npc

/**
 * @author Tom <rspsmods@gmail.com>
 */
fun Npc.prepareAtttack(combatClass: CombatClass, style: CombatStyle) {
    this.combatClass = combatClass
    this.combatStyle = style
}