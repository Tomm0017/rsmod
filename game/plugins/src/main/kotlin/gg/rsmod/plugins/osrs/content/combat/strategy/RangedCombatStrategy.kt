package gg.rsmod.plugins.osrs.content.combat.strategy

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.osrs.api.AttackStyle
import gg.rsmod.plugins.osrs.api.EquipmentType
import gg.rsmod.plugins.osrs.api.cfg.Items
import gg.rsmod.plugins.osrs.api.helper.getEquipment
import gg.rsmod.plugins.osrs.content.combat.CombatConfigs

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RangedCombatStrategy : CombatStrategy {

    private const val DEFAULT_ATTACK_RANGE = 7

    private const val MAX_ATTACK_RANGE = 10

    private val KNIVES = arrayOf(
            Items.BRONZE_KNIFE, Items.BRONZE_KNIFEP, Items.BRONZE_KNIFEP_5654, Items.BRONZE_KNIFEP_5661,
            Items.IRON_KNIFE, Items.IRON_KNIFEP, Items.IRON_KNIFEP_5655, Items.IRON_KNIFEP_5662,
            Items.STEEL_KNIFE, Items.STEEL_KNIFEP, Items.STEEL_KNIFEP_5656, Items.STEEL_KNIFEP_5663,
            Items.BLACK_KNIFE, Items.BLACK_KNIFEP, Items.BLACK_KNIFEP_5658, Items.BLACK_KNIFEP_5665,
            Items.MITHRIL_KNIFE, Items.MITHRIL_KNIFEP, Items.MITHRIL_KNIFEP_5657, Items.MITHRIL_KNIFEP_5664,
            Items.ADAMANT_KNIFE, Items.ADAMANT_KNIFEP, Items.ADAMANT_KNIFEP_5659, Items.ADAMANT_KNIFEP_5666,
            Items.RUNE_KNIFE, Items.RUNE_KNIFEP, Items.RUNE_KNIFEP_5660, Items.RUNE_KNIFEP_5667)

    private val DARTS = arrayOf(
            Items.BRONZE_DART, Items.BRONZE_DARTP, Items.BRONZE_DARTP_5628, Items.BRONZE_DARTP_5635,
            Items.IRON_DART, Items.IRON_DARTP, Items.IRON_DARTP_5629, Items.IRON_DARTP_5636,
            Items.STEEL_DART, Items.STEEL_DARTP, Items.STEEL_DARTP_5630, Items.STEEL_DARTP_5637,
            Items.BLACK_DART, Items.BLACK_DARTP, Items.BLACK_DARTP_5631, Items.BLACK_DARTP_5638,
            Items.MITHRIL_DART, Items.MITHRIL_DARTP, Items.MITHRIL_DARTP_5632, Items.MITHRIL_DARTP_5639,
            Items.ADAMANT_DART, Items.ADAMANT_DARTP, Items.ADAMANT_DARTP_5633, Items.ADAMANT_DARTP_5640,
            Items.RUNE_DART, Items.RUNE_DARTP, Items.RUNE_DARTP_5634, Items.RUNE_DARTP_5641,
            Items.DRAGON_DART, Items.DRAGON_DARTP, Items.DRAGON_DARTP_11233, Items.DRAGON_DARTP_11234)

    private val CRYSTAL_BOWS = arrayOf(
            Items.NEW_CRYSTAL_BOW, Items.NEW_CRYSTAL_BOW_I,
            Items.CRYSTAL_BOW_FULL, Items.CRYSTAL_BOW_FULL_I,
            Items.CRYSTAL_BOW_110, Items.CRYSTAL_BOW_110_I,
            Items.CRYSTAL_BOW_210, Items.CRYSTAL_BOW_210_I,
            Items.CRYSTAL_BOW_310, Items.CRYSTAL_BOW_310_I,
            Items.CRYSTAL_BOW_410, Items.CRYSTAL_BOW_410_I,
            Items.CRYSTAL_BOW_510, Items.CRYSTAL_BOW_510_I,
            Items.CRYSTAL_BOW_610, Items.CRYSTAL_BOW_610_I,
            Items.CRYSTAL_BOW_710, Items.CRYSTAL_BOW_710_I,
            Items.CRYSTAL_BOW_810, Items.CRYSTAL_BOW_810_I,
            Items.CRYSTAL_BOW_910, Items.CRYSTAL_BOW_910_I)

    private val LONG_BOWS = arrayOf(
            Items.LONGBOW, Items.OAK_LONGBOW, Items.MAPLE_LONGBOW,
            Items.WILLOW_LONGBOW, Items.YEW_LONGBOW, Items.MAGIC_LONGBOW)

    override fun getAttackRange(pawn: Pawn): Int {
        if (pawn is Player) {
            val weapon = pawn.getEquipment(EquipmentType.WEAPON)
            val attackStyle = CombatConfigs.getAttackStyle(pawn)

            var range = when (weapon?.id) {
                Items.ARMADYL_CROSSBOW -> 8
                Items.CRAWS_BOW, Items.CRAWS_BOW_U -> 10
                Items.CHINCHOMPA_10033, Items.RED_CHINCHOMPA_10034, Items.BLACK_CHINCHOMPA -> 9
                in LONG_BOWS -> 9
                in KNIVES -> 6
                in DARTS -> 3
                in CRYSTAL_BOWS -> 10
                else -> DEFAULT_ATTACK_RANGE
            }

            if (attackStyle == AttackStyle.LONG_RANGE) {
                range += 2
            }

            return Math.min(MAX_ATTACK_RANGE, range)
        }
        return DEFAULT_ATTACK_RANGE
    }

    override fun attack(pawn: Pawn, target: Pawn) {
        val animation = CombatConfigs.getAttackAnimation(pawn)
        pawn.animate(animation)
    }

    override fun getHitDelay(start: Tile, target: Tile): Int {
        val distance = start.getDistance(target)
        return 2 + (Math.floor((3.0 + distance) / 6.0)).toInt()
    }
}