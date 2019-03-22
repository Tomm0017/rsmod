package gg.rsmod.plugins.content.skills.mining

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.content.skills.mining.Mining.Rock

val rocks = ImmutableSet.of(
        /**
         * TODO: Complete Rock List
         * [ ] Rune Essence
         * [x] Clay
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
         * [x] Adamantite
         * [x] Runite
         * [x] Amethyst
         */
        Rock(RockType.CLAY, obj = 7454, depletedOre = 7468),
        Rock(RockType.CLAY, obj = 7487, depletedOre = 7469),
        Rock(RockType.COPPER, obj = 7453, depletedOre = 7468),
        Rock(RockType.COPPER, obj = 7484, depletedOre = 7469),
        Rock(RockType.TIN, obj = 7485, depletedOre = 7468),
        Rock(RockType.TIN, obj = 7486, depletedOre = 7469),
        Rock(RockType.IRON, obj = 7455, depletedOre = 7468),
        Rock(RockType.IRON, obj = 7488, depletedOre = 7469),
        Rock(RockType.COAL, obj = 7456, depletedOre = 7469),
        Rock(RockType.COAL, obj = 7489, depletedOre = 7468),
        Rock(RockType.MITHRIL, obj = 7459, depletedOre = 7468),
        Rock(RockType.MITHRIL, obj = 7492, depletedOre = 7469),
        Rock(RockType.ADAMANTITE, obj = 7460, depletedOre = 7469),
        Rock(RockType.ADAMANTITE, obj = 7493, depletedOre = 7469),
        Rock(RockType.RUNITE, obj = 7461, depletedOre = 7469),
        Rock(RockType.RUNITE, obj = 7494, depletedOre = 7469),
        Rock(RockType.AMETHYST, obj = 30371, depletedOre = 30373),
        Rock(RockType.AMETHYST, obj = 30372, depletedOre = 30373)


)!!

rocks.forEach { rock ->
    on_obj_option(obj = rock.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue { Mining.mineOre(this, obj, rock.type, rock.depletedOre) }
    }
}