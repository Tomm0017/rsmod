package gg.rsmod.plugins.content.combat.strategy.ranged.weapon

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.ADAMANT_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.AMETHYST_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRONZE_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_ADAMANT_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_BLACK_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_BRONZE_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_IRON_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_MITHRIL_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_RUNE_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.BRUTAL_STEEL_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.DRAGON_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.IRON_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.MITHRIL_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.OGRE_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.RUNE_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.STEEL_ARROWS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Arrows.TRAINING_ARROWS

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class BowType(val item: Int, val ammo: Array<Int>) {

    TRAINING_BOW(item = Items.TRAINING_BOW, ammo = TRAINING_ARROWS),

    SHORTBOW(item = Items.SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS),
    LONGBOW(item = Items.LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS),

    OAK_SHORTBOW(item = Items.OAK_SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS),
    OAK_LONGBOW(item = Items.OAK_LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS),

    WILLOW_SHORTBOW(item = Items.WILLOW_SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),
    WILLOW_COMP_BOW(item = Items.WILLOW_COMP_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),
    WILLOW_LONGBOW(item = Items.WILLOW_LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS),

    MAPLE_SHORTBOW(item = Items.MAPLE_SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS),
    MAPLE_LONGBOW(item = Items.MAPLE_LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS),

    OGRE_BOW(item = Items.OGRE_BOW, ammo = OGRE_ARROWS),
    COMP_OGRE_BOW(item = Items.COMP_OGRE_BOW, ammo = BRUTAL_BRONZE_ARROWS + BRUTAL_IRON_ARROWS + BRUTAL_STEEL_ARROWS + BRUTAL_BLACK_ARROWS + BRUTAL_MITHRIL_ARROWS + BRUTAL_ADAMANT_ARROWS + BRUTAL_RUNE_ARROWS),

    YEW_SHORTBOW(item = Items.YEW_SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS),
    YEW_LONGBOW(item = Items.YEW_LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS),
    YEW_COMP_BOW(item = Items.YEW_COMP_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS),

    MAGIC_SHORTBOW(item = Items.MAGIC_SHORTBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS),
    MAGIC_SHORTBOW_I(item = Items.MAGIC_SHORTBOW_I, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS),
    MAGIC_LONGBOW(item = Items.MAGIC_LONGBOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS),
    MAGIC_COMP_BOW(item = Items.MAGIC_COMP_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS),

    SEERCULL(item = Items.SEERCULL, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS),
    CRAWS_BOW(item = Items.CRAWS_BOW, ammo = emptyArray()),

    DARK_BOW(item = Items.DARK_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),
    BLUE_DARK_BOW(item = Items.DARK_BOW_12765, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),
    GREEN_DARK_BOW(item = Items.DARK_BOW_12766, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),
    WHITE_DARK_BOW(item = Items.DARK_BOW_12767, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),
    YELLOW_DARK_BOW(item = Items.DARK_BOW_12768, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),

    THIRD_AGE_BOW(item = Items._3RD_AGE_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS),

    CRYSTAL_BOW_110(item = Items.CRYSTAL_BOW_110, ammo = emptyArray()),
    CRYSTAL_BOW_210(item = Items.CRYSTAL_BOW_210, ammo = emptyArray()),
    CRYSTAL_BOW_310(item = Items.CRYSTAL_BOW_310, ammo = emptyArray()),
    CRYSTAL_BOW_410(item = Items.CRYSTAL_BOW_410, ammo = emptyArray()),
    CRYSTAL_BOW_510(item = Items.CRYSTAL_BOW_510, ammo = emptyArray()),
    CRYSTAL_BOW_610(item = Items.CRYSTAL_BOW_610, ammo = emptyArray()),
    CRYSTAL_BOW_710(item = Items.CRYSTAL_BOW_710, ammo = emptyArray()),
    CRYSTAL_BOW_810(item = Items.CRYSTAL_BOW_810, ammo = emptyArray()),
    CRYSTAL_BOW_910(item = Items.CRYSTAL_BOW_910, ammo = emptyArray()),
    CRYSTAL_BOW_FULL(item = Items.CRYSTAL_BOW_FULL, ammo = emptyArray()),
    CRYSTAL_BOW_NEW(item = Items.NEW_CRYSTAL_BOW, ammo = emptyArray()),

    TWISTED_BOW(item = Items.TWISTED_BOW, ammo = BRONZE_ARROWS + IRON_ARROWS + STEEL_ARROWS + MITHRIL_ARROWS + ADAMANT_ARROWS + RUNE_ARROWS + AMETHYST_ARROWS + DRAGON_ARROWS);

    companion object {
        val values = enumValues<BowType>()
    }
}