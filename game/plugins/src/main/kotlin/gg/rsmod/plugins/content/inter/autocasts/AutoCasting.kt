package gg.rsmod.plugins.content.inter.autocasts

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.InterfaceDestination
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.combat.Combat

object AutoCasting {
    
    const val INTERFACE_ID = 201
    const val AUTO_CAST_SPELL_LIST_VARP = 664
    
    private val UNIQUE_STAFF_LIST =
            arrayOf(Items.SLAYERS_STAFF, Items.VOID_KNIGHT_MACE,Items.IBANS_STAFF,
            Items.STAFF_OF_THE_DEAD,Items.STAFF_OF_LIGHT,Items.STAFF_OF_BALANCE)

    fun openAutoCastSpellsList(player : Player) {
        player.openInterface(INTERFACE_ID, InterfaceDestination.ATTACK)
        player.getEquipment(EquipmentType.WEAPON)?.let { getStaffSpellList(player, it) }?.let { player.setVarp(AUTO_CAST_SPELL_LIST_VARP, it) }
        player.setInterfaceEvents(interfaceId = INTERFACE_ID, component = 1, from = 0, to = 51, setting = 2)

    }

    private fun getStaffSpellList(player: Player, equipped_weapon : Item): Int {
        when(equipped_weapon.id in UNIQUE_STAFF_LIST) { true -> return equipped_weapon.id }
        when(equipped_weapon.getName(player.world.definitions).contains("Staff", ignoreCase = true)) { true-> return -1}
        return 0
    }

    fun disableSelectedAutoCastSpell(player : Player) {
        player.attr.remove(Combat.CASTING_SPELL)
        player.setVarbit(Combat.SELECTED_AUTOCAST_VARBIT, 0)
    }
}