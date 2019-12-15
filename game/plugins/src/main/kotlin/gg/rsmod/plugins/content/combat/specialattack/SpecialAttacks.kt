package gg.rsmod.plugins.content.combat.specialattack

import gg.rsmod.game.model.LockState
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.getEquipment
import gg.rsmod.plugins.api.ext.message
import gg.rsmod.plugins.content.inter.attack.AttackTab

/**
 * @author Tom <rspsmods@gmail.com>
 */
object SpecialAttacks {

    fun register(weapons: IntArray, energy: Int, excutionType: ExecutionType, attack: CombatContext.() -> Unit) {
        weapons.forEach { weapon -> attacks[weapon] = SpecialAttack(energy, excutionType,  attack) }
    }

     fun execute(player: Player, target: Pawn?, world: World, executionType: ExecutionType): Boolean {
         val weaponItem = player.getEquipment(EquipmentType.WEAPON) ?: return false
         val special = attacks[weaponItem.id] ?: return false
         if (executionType != special.executeType) return false

         AttackTab.disableSpecial(player)
         if (AttackTab.getEnergy(player) < special.energyRequired) {
             player.message("You don't have enough power left.")
             return false
         }

         player.lock = LockState.DELAY_ACTIONS
         AttackTab.setEnergy(player, AttackTab.getEnergy(player) - special.energyRequired)

         val combatContext = CombatContext(world, player)
         target?.let { combatContext.target = it }
         special.attack(combatContext)
         player.unlock()

         return true
     }

    fun ignoreDelay(player: Player): Boolean {
        val weaponItem = player.getEquipment(EquipmentType.WEAPON) ?: return false
        val special = attacks[weaponItem.id] ?: return false

        if (AttackTab.getEnergy(player) < special.energyRequired) {
            return false
        }

        /*
        Weapons that ignore delay
         */
        when (special == attacks[Items.GRANITE_MAUL]) {true -> return true}

        return false
    }

    private val attacks = mutableMapOf<Int, SpecialAttack>()
}