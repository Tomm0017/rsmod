package gg.rsmod.plugins.content.skills.mining

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.content.skills.mining.Mining.Rock

val rocks = ImmutableSet.of(
        /**
         * TODO: Complete Rock List
         * [ ] Rune Essence
         * [ ] Clay
         * [x] Copper
         * [x] Tin
         * [ ] Blurite
         * [ ] Limestone
         * [x] Iron
         * [ ] Silver
         * [x] Coal
         * [ ] Pure Essence
         * [ ] Sandstone
         * [ ] Gold
         * [ ] Gem Rock
         * [ ] Granite
         * [x] Mithril
         * [ ] Lovakite
         * [ ] Adamantite
         * [ ] Runite
         * [ ] Amethyst
         */

        /**
         * FIRST SET
         */
        Rock(RockType.COPPER, obj = 7453, depletedOre = 0),
        // 7454
        Rock(RockType.IRON, obj = 7455, depletedOre = 0),
        Rock(RockType.COAL, obj = 7456, depletedOre = 0),
        // 7457
        // 7458
        Rock(RockType.MITHRIL, obj = 7459, depletedOre = 0),
        Rock(RockType.ADAMANTITE, obj = 7460, depletedOre = 0),
        Rock(RockType.RUNITE, obj = 7461, depletedOre = 0),

        /**
         * SECOND SET
         */
        Rock(RockType.COPPER, obj = 7484, depletedOre = 0),
        Rock(RockType.TIN, obj = 7485, depletedOre = 0),
        Rock(RockType.TIN, obj = 7486, depletedOre = 0),
        // 7487
        Rock(RockType.IRON, obj = 7488, depletedOre = 0),
        Rock(RockType.COAL, obj = 7489, depletedOre = 0),
        // 7490
        // 7491
        // 7482
        Rock(RockType.MITHRIL, obj = 7492, depletedOre = 0),
        Rock(RockType.ADAMANTITE, obj = 7493, depletedOre = 0)
)!!

rocks.forEach { rock ->
    on_obj_option(obj = rock.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue { Mining.mineOre(this, obj, rock.type, rock.depletedOre) }
    }
}