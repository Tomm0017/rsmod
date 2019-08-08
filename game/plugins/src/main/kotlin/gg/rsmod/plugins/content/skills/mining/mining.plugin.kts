package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.api.ext.getInteractingGameObj
import gg.rsmod.plugins.api.ext.player
import gg.rsmod.plugins.content.skills.mining.Mining.Ore

/**
 * @author Anthony Loukinas <anthony.loukinas@gmail.com>
 */
private val ORES = setOf(
        // TODO: Need to find the alternate emptyOreId rock
        Ore(OreType.RUNE_ESSENCE, obj = 34773, emptyOreId = 0), // X = 2911 Y = 4832 - Essence Mine

        Ore(OreType.CLAY, obj = 11362, emptyOreId = 2704),
        Ore(OreType.CLAY, obj = 11363, emptyOreId = 11391),

        Ore(OreType.COPPER, obj = 10943, emptyOreId = 2704),
        Ore(OreType.COPPER, obj = 11161, emptyOreId = 11391),

        Ore(OreType.TIN, obj = 11360, emptyOreId = 2704),
        Ore(OreType.TIN, obj = 11361, emptyOreId = 11391),

        Ore(OreType.BLURITE, obj = 11378, emptyOreId = 2704),
        Ore(OreType.BLURITE, obj = 11379, emptyOreId = 11391),

        // TODO: Find Limestone emptyOreId
        Ore(OreType.LIMESTONE, obj = 11382, emptyOreId = 0), // Need to figure out emptyOreId

        Ore(OreType.IRON, obj = 11364, emptyOreId = 2704),
        Ore(OreType.IRON, obj = 11365, emptyOreId = 11391),

        Ore(OreType.SILVER, obj = 11368, emptyOreId = 2704),
        Ore(OreType.SILVER, obj = 11369, emptyOreId = 11391),

        Ore(OreType.COAL, obj = 11366, emptyOreId = 2704),
        Ore(OreType.COAL, obj = 11367, emptyOreId = 11391),

        Ore(OreType.SANDSTONE1, obj = 11386, emptyOreId = 2704),

        Ore(OreType.GOLD, obj = 11370, emptyOreId = 2704),
        Ore(OreType.GOLD, obj = 11371, emptyOreId = 11391),

        Ore(OreType.GEMSTONE1, obj = 11381, emptyOreId = 11391),
        Ore(OreType.GEMSTONE1, obj = 11380, emptyOreId = 11391),

        Ore(OreType.GRANITE1, obj = 11387, emptyOreId = 2704),

        Ore(OreType.MITHRIL, obj = 11372, emptyOreId = 2704),
        Ore(OreType.MITHRIL, obj = 11373, emptyOreId = 11391),

        Ore(OreType.ADAMANTITE, obj = 11374, emptyOreId = 2704),
        Ore(OreType.ADAMANTITE, obj = 11375, emptyOreId = 11391),

        Ore(OreType.RUNITE, obj = 11376, emptyOreId = 2704),
        Ore(OreType.RUNITE, obj = 11377, emptyOreId = 11391)
)

ORES.forEach { ore ->
    /**
     * Adds ore mining logic to option 1 - "Mine $ore"
     */
    on_obj_option(obj = ore.obj, option = 1) {
        val obj = player.getInteractingGameObj()

        player.queue {
            Mining.mineOre(this, obj, ore.type, ore.emptyOreId)
        }
    }

    /**
     * Adds ore prospecting logic to option 2 - "Prospect $ore"
     */
    on_obj_option(obj = ore.obj, option = 2) {
        player.queue {
            Mining.prospectOre(this, ore.type)
        }
    }
}