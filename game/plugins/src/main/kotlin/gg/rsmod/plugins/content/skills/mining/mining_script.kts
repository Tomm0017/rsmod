package gg.rsmod.plugins.content.skills.mining

import com.google.common.collect.ImmutableSet
import gg.rsmod.plugins.content.skills.mining.Mining.Rock

val rocks = ImmutableSet.of(
        Rock(RockType.RUNE_ESSENCE, obj = 7471, depletedOre = 0), // depletedOre NOT AVAILABLE
        Rock(RockType.CLAY, obj = 7454, depletedOre = 7468),
        Rock(RockType.CLAY, obj = 7487, depletedOre = 7469),
        Rock(RockType.COPPER, obj = 7453, depletedOre = 7468),
        Rock(RockType.COPPER, obj = 7484, depletedOre = 7469),
        Rock(RockType.TIN, obj = 7485, depletedOre = 7468),
        Rock(RockType.TIN, obj = 7486, depletedOre = 7469),
        Rock(RockType.BLURITE, obj = 7462, depletedOre = 7468),
        Rock(RockType.BLURITE, obj = 7495, depletedOre = 7469),
        Rock(RockType.IRON, obj = 7455, depletedOre = 7468),
        Rock(RockType.IRON, obj = 7488, depletedOre = 7469),
        Rock(RockType.SILVER, obj = 7457, depletedOre = 7468),
        Rock(RockType.SILVER, obj = 7490, depletedOre = 7469),
        Rock(RockType.ELEMENTAL, obj = 7463, depletedOre = 7468),
        Rock(RockType.ELEMENTAL, obj = 7464, depletedOre = 7469),
        Rock(RockType.DAEYAL, obj = 17962, depletedOre = 0), // depletedOre NOT AVAILABLE
        Rock(RockType.COAL, obj = 7456, depletedOre = 7468),
        Rock(RockType.COAL, obj = 7489, depletedOre = 7469),
        Rock(RockType.GOLD, obj = 7458, depletedOre = 7468),
        Rock(RockType.GOLD, obj = 7491, depletedOre = 7469),
        Rock(RockType.MITHRIL, obj = 7459, depletedOre = 7468),
        Rock(RockType.MITHRIL, obj = 7492, depletedOre = 7469),
        Rock(RockType.LOVAKITE, obj = 28596, depletedOre = 7468),
        Rock(RockType.LOVAKITE, obj = 28597, depletedOre = 7469),
        Rock(RockType.ADAMANTITE, obj = 7460, depletedOre = 7468),
        Rock(RockType.ADAMANTITE, obj = 7493, depletedOre = 7469),
        Rock(RockType.RUNITE, obj = 7461, depletedOre = 7468),
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