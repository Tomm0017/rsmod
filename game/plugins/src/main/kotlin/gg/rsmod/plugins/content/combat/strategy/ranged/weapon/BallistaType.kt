package gg.rsmod.plugins.content.combat.strategy.ranged.weapon

import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.ADAMANT_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.AMETHYST_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.BRONZE_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.DRAGON_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.IRON_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.MITHRIL_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.RUNE_JAVELINS
import gg.rsmod.plugins.content.combat.strategy.ranged.ammo.Javelins.STEEL_JAVELINS

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class BallistaType(val item: Int, val ammo: Array<Int>) {

    LIGHT_BALLISTA(item = Items.LIGHT_BALLISTA, ammo = BRONZE_JAVELINS + IRON_JAVELINS + STEEL_JAVELINS + MITHRIL_JAVELINS + ADAMANT_JAVELINS + RUNE_JAVELINS + AMETHYST_JAVELINS + DRAGON_JAVELINS),
    HEAVY_BALLISTA(item = Items.HEAVY_BALLISTA, ammo = BRONZE_JAVELINS + IRON_JAVELINS + STEEL_JAVELINS + MITHRIL_JAVELINS + ADAMANT_JAVELINS + RUNE_JAVELINS + AMETHYST_JAVELINS + DRAGON_JAVELINS);

    
    companion object {
        val values = enumValues<BallistaType>()
    }
}

