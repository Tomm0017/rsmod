package gg.rsmod.plugins.osrs.content.combat.strategy.ranged

import gg.rsmod.game.model.Graphic
import gg.rsmod.plugins.osrs.api.ProjectileType
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.ADAMANT_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.AMETHYST_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.BRONZE_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.DRAGON_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.IRON_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.MITHRIL_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.OGRE_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.RUNE_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Arrows.STEEL_ARROWS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Bolts
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.ADAMANT_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.BLACK_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.BRONZE_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.DRAGON_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.IRON_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.MITHRIL_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.RUNE_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Darts.STEEL_DARTS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.ADAMANT_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.AMETHYST_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.BRONZE_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.DRAGON_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.IRON_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.MITHRIL_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.RUNE_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Javelins.STEEL_JAVELINS
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.ADAMANT_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.BLACK_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.BRONZE_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.DRAGON_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.IRON_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.MITHRIL_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.RUNE_KNIVES
import gg.rsmod.plugins.osrs.content.combat.strategy.ranged.ammo.Knives.STEEL_KNIVES

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class RangedProjectile(val gfx: Int, val drawback: Graphic? = null, val type: ProjectileType, val items: Array<Int>) {

    BOLTS(gfx = 27, type = ProjectileType.BOLT, items = Bolts.BRONZE_BOLTS + Bolts.IRON_BOLTS + Bolts.STEEL_BOLTS + Bolts.MITHRIL_BOLTS
            + Bolts.ADAMANT_BOLTS + Bolts.RUNITE_BOLTS + Bolts.DRAGON_BOLTS + Bolts.BLURITE_BOLTS + Bolts.KEBBIT_BOLTS + Bolts.BONE_BOLTS),

    BRONZE_ARROW(gfx = 10, drawback = Graphic(id = 19, height = 96), type = ProjectileType.ARROW, items = BRONZE_ARROWS),
    IRON_ARROW(gfx = 9, drawback = Graphic(id = 18, height = 96), type = ProjectileType.ARROW, items = IRON_ARROWS),
    STEEL_ARROW(gfx = 11, drawback = Graphic(id = 20, height = 96), type = ProjectileType.ARROW, items = STEEL_ARROWS),
    MITHRIL_ARROW(gfx = 12, drawback = Graphic(id = 21, height = 96), type = ProjectileType.ARROW, items = MITHRIL_ARROWS),
    ADAMANT_ARROW(gfx = 13, drawback = Graphic(id = 22, height = 96), type = ProjectileType.ARROW, items = ADAMANT_ARROWS),
    RUNE_ARROW(gfx = 15, drawback = Graphic(id = 24, height = 96), type = ProjectileType.ARROW, items = RUNE_ARROWS),
    AMETHYST_ARROW(gfx = 1384, drawback = Graphic(id = 1385, height = 96), type = ProjectileType.ARROW, items = AMETHYST_ARROWS),
    DRAGON_ARROW(gfx = 1120, drawback = Graphic(id = 1111, height = 96), type = ProjectileType.ARROW, items = DRAGON_ARROWS),
    OGRE_ARROW(gfx = 242, drawback = Graphic(id = 243, height = 50), type = ProjectileType.ARROW, items = OGRE_ARROWS),
    // TODO: dragon arrows

    BRONZE_JAVELIN(gfx = 200, type = ProjectileType.JAVELIN, items = BRONZE_JAVELINS),
    IRON_JAVELIN(gfx = 201, type = ProjectileType.JAVELIN, items = IRON_JAVELINS),
    STEEL_JAVELIN(gfx = 202, type = ProjectileType.JAVELIN, items = STEEL_JAVELINS),
    MITHRIL_JAVELIN(gfx = 203, type = ProjectileType.JAVELIN, items = MITHRIL_JAVELINS),
    ADAMANT_JAVELIN(gfx = 204, type = ProjectileType.JAVELIN, items = ADAMANT_JAVELINS),
    RUNE_JAVELIN(gfx = 205, type = ProjectileType.JAVELIN, items = RUNE_JAVELINS),
    AMETHYST_JAVELIN(gfx = 1386, type = ProjectileType.JAVELIN, items = AMETHYST_JAVELINS),
    DRAGON_JAVELIN(gfx = 1301, type = ProjectileType.JAVELIN, items = DRAGON_JAVELINS),

    BRONZE_KNIFE(gfx = 212, drawback = Graphic(id = 219, height = 96), type = ProjectileType.KNIFE, items = BRONZE_KNIVES),
    IRON_KNIFE(gfx = 213, drawback = Graphic(id = 220, height = 96), type = ProjectileType.KNIFE, items = IRON_KNIVES),
    STEEL_KNIFE(gfx = 214, drawback = Graphic(id = 221, height = 96), type = ProjectileType.KNIFE, items = STEEL_KNIVES),
    BLACK_KNIFE(gfx = 215, drawback = Graphic(id = 222, height = 96), type = ProjectileType.KNIFE, items = BLACK_KNIVES),
    MITHRIL_KNIFE(gfx = 216, drawback = Graphic(id = 223, height = 96), type = ProjectileType.KNIFE, items = MITHRIL_KNIVES),
    ADAMANT_KNIFE(gfx = 217, drawback = Graphic(id = 224, height = 96), type = ProjectileType.KNIFE, items = ADAMANT_KNIVES),
    RUNE_KNIFE(gfx = 218, drawback = Graphic(id = 225, height = 96), type = ProjectileType.KNIFE, items = RUNE_KNIVES),
    DRAGON_KNIFE(gfx = 28, type = ProjectileType.KNIFE, items = DRAGON_KNIVES),

    BRONZE_DART(gfx = 226, drawback = Graphic(id = 232, height = 96), type = ProjectileType.DART, items = BRONZE_DARTS),
    IRON_DART(gfx = 227, drawback = Graphic(id = 233, height = 96), type = ProjectileType.DART, items = IRON_DARTS),
    STEEL_DART(gfx = 228, drawback = Graphic(id = 234, height = 96), type = ProjectileType.DART, items = STEEL_DARTS),
    BLACK_DART(gfx = 229, drawback = Graphic(id = 235, height = 96), type = ProjectileType.DART, items = BLACK_DARTS), // TODO: get real projectile/drawback id
    MITHRIL_DART(gfx = 229, drawback = Graphic(id = 235, height = 96), type = ProjectileType.DART, items = MITHRIL_DARTS),
    ADAMANT_DART(gfx = 230, drawback = Graphic(id = 236, height = 96), type = ProjectileType.DART, items = ADAMANT_DARTS),
    RUNE_DART(gfx = 231, drawback = Graphic(id = 237, height = 96), type = ProjectileType.DART, items = RUNE_DARTS),
    DRAGON_DART(gfx = 1122, drawback = Graphic(id = 1123, height = 96), type = ProjectileType.DART, items = DRAGON_DARTS),
}