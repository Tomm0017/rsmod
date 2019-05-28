package gg.rsmod.plugins.content.combat.specialattack

import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.ext.getEquipment
import gg.rsmod.plugins.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialAttacks {

    fun register(weapon: Int, energy: Int, attack: CombatContext.() -> Unit) {
        attacks[weapon] = SpecialAttack(energy, attack)
    }

    fun execute(player: Player, target: Pawn?, world: World): Boolean {
        val weaponItem = player.getEquipment(EquipmentType.WEAPON) ?: return false
        val special = attacks[weaponItem.id] ?: return false

        if (AttackTab.getEnergy(player) < special.energyRequired) {
            return false
        }
        
        AttackTab.setEnergy(player, AttackTab.getEnergy(player) - special.energyRequired)

        val combatContext = CombatContext(world, player)
        target?.let { combatContext.target = it }
        special.attack(combatContext)

        return true
    }

    private val attacks = mutableMapOf<Int, SpecialAttack>()
}