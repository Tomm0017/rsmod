package gg.rsmod.plugins.osrs.content.inter.equipstats

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.BonusSlot
import gg.rsmod.plugins.osrs.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipmentStats {

    const val INTERFACE_ID = 84
    const val TAB_INTERFACE_ID = 85

    private fun formatBonus(p: Player, slot: BonusSlot): String = formatBonus(p.getBonus(slot))

    private fun formatBonus(bonus: Int): String = if (bonus < 0) bonus.toString() else "+$bonus"

    fun sendBonuses(p: Player) {
        // TODO: these two bonuses
        var undeadBonus = 0.0
        var slayerBonus = 0.0

        var index: Int

        index = 23
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Stab: ${formatBonus(p, BonusSlot.ATTACK_STAB)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Slash: ${formatBonus(p, BonusSlot.ATTACK_SLASH)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Crush: ${formatBonus(p, BonusSlot.ATTACK_CRUSH)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Magic: ${formatBonus(p, BonusSlot.ATTACK_MAGIC)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index, text = "Range: ${formatBonus(p, BonusSlot.ATTACK_RANGED)}")

        index = 29
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Stab: ${formatBonus(p, BonusSlot.DEFENCE_STAB)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Slash: ${formatBonus(p, BonusSlot.DEFENCE_SLASH)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Crush: ${formatBonus(p, BonusSlot.DEFENCE_CRUSH)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Range: ${formatBonus(p, BonusSlot.DEFENCE_RANGED)}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index, text = "Magic: ${formatBonus(p, BonusSlot.DEFENCE_MAGIC)}")

        index = 35
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Melee strength: ${formatBonus(p.getStrengthBonus())}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Ranged strength: ${formatBonus(p.getRangedStrengthBonus())}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index, text = "Magic damage: ${formatBonus(p.getMagicDamageBonus())}%")

        val undead = if (undeadBonus == 0.0) "0" else String.format("%.2f", undeadBonus)
        val slayer = if (slayerBonus == 0.0) "0" else String.format("%.2f", slayerBonus)

        p.setComponentText(interfaceId = INTERFACE_ID, component = 38, text = "Prayer: ${formatBonus(p.getPrayerBonus())}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = 40, text = "Undead: $undead%")
        p.setComponentText(interfaceId = INTERFACE_ID, component = 41, text = "Slayer: $slayer%")
    }
}