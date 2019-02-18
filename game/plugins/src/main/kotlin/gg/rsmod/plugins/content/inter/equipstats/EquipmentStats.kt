package gg.rsmod.plugins.content.inter.equipstats

import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.BonusSlot
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object EquipmentStats {

    const val INTERFACE_ID = 84
    const val TAB_INTERFACE_ID = 85

    private val MAGE_ELITE_VOID = intArrayOf(Items.VOID_MAGE_HELM, Items.ELITE_VOID_TOP, Items.ELITE_VOID_ROBE, Items.VOID_KNIGHT_GLOVES)

    private fun formatBonus(p: Player, slot: BonusSlot): String = EquipmentStats.formatBonus(p.getBonus(slot))

    private fun formatBonus(bonus: Int): String = if (bonus < 0) bonus.toString() else "+$bonus"

    private fun formatBonus(bonus: Double): String {
        val format = String.format("%.1f", bonus)
        return if (bonus < 0) format else "+$format"
    }

    fun sendBonuses(p: Player) {
        // TODO: these two bonuses
        var undeadBonus = 0.0
        var slayerBonus = 0.0
        var magicDamageBonus = p.getMagicDamageBonus().toDouble()

        if (p.hasEquipped(MAGE_ELITE_VOID)) {
            magicDamageBonus += 2.5
        }

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
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Melee strength: ${EquipmentStats.formatBonus(p.getStrengthBonus())}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index++, text = "Ranged strength: ${EquipmentStats.formatBonus(p.getRangedStrengthBonus())}")
        p.setComponentText(interfaceId = INTERFACE_ID, component = index, text = "Magic damage: ${EquipmentStats.formatBonus(magicDamageBonus)}%")
        p.setComponentText(interfaceId = INTERFACE_ID, component = 38, text = "Prayer: ${EquipmentStats.formatBonus(p.getPrayerBonus())}")

        val undead = String.format("%.1f", undeadBonus)
        val slayer = String.format("%.1f", slayerBonus)

        p.setComponentText(interfaceId = INTERFACE_ID, component = 40, text = "Undead: $undead%")
        p.setComponentText(interfaceId = INTERFACE_ID, component = 41, text = "Slayer: $slayer%")
    }
}