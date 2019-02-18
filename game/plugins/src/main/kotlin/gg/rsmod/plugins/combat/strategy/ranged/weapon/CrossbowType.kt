package gg.rsmod.plugins.content.combat.strategy.ranged.weapon

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.ADAMANT_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.BLURITE_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.BOLT_RACKS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.BONE_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.BROAD_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.BRONZE_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.DRAGON_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.IRON_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.KEBBIT_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.MITHRIL_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.RUNITE_BOLTS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Bolts.STEEL_BOLTS

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class CrossbowType(val item: Int, val ammo: Array<Int>) {

    PHOENIX_CROSSBOW(item = Items.PHOENIX_CROSSBOW, ammo = BRONZE_BOLTS),
    CROSSBOW(item = Items.CROSSBOW, ammo = BRONZE_BOLTS),

    BRONZE_CROSSBOW(item = Items.BRONZE_CROSSBOW, ammo = BRONZE_BOLTS),
    IRON_CROSSBOW(item = Items.IRON_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS),
    STEEL_CROSSBOW(item = Items.STEEL_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS),
    MITH_CROSSBOW(item = Items.MITH_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + MITHRIL_BOLTS),
    ADAMANT_CROSSBOW(item = Items.ADAMANT_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS),
    RUNE_CROSSBOW(item = Items.RUNE_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS),
    DRAGON_CROSSBOW(item = Items.DRAGON_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS),

    DRAGON_HUNTER_CROSSBOW(item = Items.DRAGON_HUNTER_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS),
    ARMADYL_CROSSBOW(item = Items.ARMADYL_CROSSBOW, ammo = BRONZE_BOLTS + IRON_BOLTS + STEEL_BOLTS + ADAMANT_BOLTS + BROAD_BOLTS + RUNITE_BOLTS + DRAGON_BOLTS),

    BLURITE_CROSSBOW(item = Items.BLURITE_CROSSBOW, ammo = BRONZE_BOLTS + BLURITE_BOLTS),
    DORGESHUUN_CROSSBOW(item = Items.DORGESHUUN_CROSSBOW, ammo = BONE_BOLTS),
    HUNTER_CROSSBOW(item = Items.HUNTERS_CROSSBOW, ammo = KEBBIT_BOLTS),

    KARIL_CROSSBOW(item = Items.KARILS_CROSSBOW, ammo = BOLT_RACKS),
    KARIL_CROSSBOW_0(item = Items.KARILS_CROSSBOW_0, ammo = BOLT_RACKS),
    KARIL_CROSSBOW_25(item = Items.KARILS_CROSSBOW_25, ammo = BOLT_RACKS),
    KARIL_CROSSBOW_50(item = Items.KARILS_CROSSBOW_50, ammo = BOLT_RACKS),
    KARIL_CROSSBOW_75(item = Items.KARILS_CROSSBOW_75, ammo = BOLT_RACKS),
    KARIL_CROSSBOW_100(item = Items.KARILS_CROSSBOW_100, ammo = BOLT_RACKS);

    companion object {
        val values = enumValues<BowType>()
    }
}

