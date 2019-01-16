package gg.rsmod.plugins.osrs.content.combat.strategy.ranged

import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Bolts

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class RangedProjectile(val gfx: Int, val type: ProjectileType, val items: Array<Int>) {
    BOLTS(gfx = 27, type = ProjectileType.BOLT, items = Bolts.BRONZE_BOLTS + Bolts.IRON_BOLTS + Bolts.STEEL_BOLTS + Bolts.MITHRIL_BOLTS
            + Bolts.ADAMANT_BOLTS + Bolts.RUNITE_BOLTS + Bolts.DRAGON_BOLTS + Bolts.BLURITE_BOLTS + Bolts.KEBBIT_BOLTS + Bolts.BONE_BOLTS),
}