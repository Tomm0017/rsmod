package gg.rsmod.plugins.content.skills.mining

import gg.rsmod.plugins.content.skills.mining.Mining.Rock

/**
 * @author Misterbaho <MisterBaho#6447>
 */

val rocks = setOf(
        Rock(RockType.CLAY, obj = 7487),
        Rock(RockType.COPPER, obj = 7484),
        Rock(RockType.COPPER, obj = 7453),
        Rock(RockType.TIN, obj = 7485),
        Rock(RockType.TIN, obj = 7486),
        Rock(RockType.RUNITE, obj = 7461),
        Rock(RockType.RUNITE, obj = 7494)
//        Rock(RockType.BLURITE, obj = 7484, emptyRock = 7469),
//        Rock(RockType.IRON, obj = 7484, emptyRock = 7469),
//        Rock(RockType.COAL, obj = 7484, emptyRock = 7469),
//        Rock(RockType.SILVER, obj = 7484, emptyRock = 7469),
//        Rock(RockType.GOLD, obj = 7484, emptyRock = 7469),
//        Rock(RockType.COAL, obj = 7484, emptyRock = 7469)
)

rocks.forEach { rock ->
    on_obj_option(obj = rock.obj, option = 1) {
        val obj = player.getInteractingGameObj()
        player.queue {
            Mining.mining(this, obj, rock.type)
        }
    }

    on_obj_option(obj = rock.obj, option = 2) {
        player.queue {
            Mining.prospect(this, rock.type)
        }
    }
}