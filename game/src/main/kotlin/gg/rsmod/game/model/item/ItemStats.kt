package gg.rsmod.game.model.item

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class ItemStats(val item: Int, val name: String, val weight: Double, val equipSlot: Int, val equipType: Int, val attackSpeed: Int, val bonuses: List<Int>)