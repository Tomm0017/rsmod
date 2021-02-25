package gg.rsmod.plugins.content.skills.thieving.objs

import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.Player
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.cfg.Items
import gg.rsmod.plugins.api.cfg.Objs
import gg.rsmod.plugins.api.ext.*
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.bakers_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.fruit_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.silk_stall_steals
import gg.rsmod.plugins.content.skills.thieving.objs.StallRewards.vegetable_stall_steals
import kotlin.random.Random

enum class Stall(val stallID: Int,  val minStealLevel: Int, val xpGain: Double, val respawnCycles: Int, val emptyStallId: Int = Objs.MARKET_STALL, val attemptMsg: String = "", val steals: Map<Int, String>) {
    VEGETABLE_STALL(Objs.VEG_STALL, 2, 10.0, 3, steals = vegetable_stall_steals),
    BAKERS_STALL(Objs.BAKERS_STALL, 5, 16.0, 4, steals = bakers_stall_steals),
    BAKERS_STALL2(Objs.BAKERS_STALL_11730, 5, 16.0, 4, steals = bakers_stall_steals),
    SILK_STALL(Objs.SILK_STALL_11729, 20, 24.0, 6, attemptMsg = "some silk", steals = silk_stall_steals),
    SILK_STALL2(Objs.SILK_STALL_36569, 20, 24.0, 6, attemptMsg = "some silk", steals = silk_stall_steals),
    FRUIT_STALL(Objs.FRUIT_STALL_28823, 25, 28.5, 3, emptyStallId = Objs.FRUIT_STALL, attemptMsg = "some fruit", steals = fruit_stall_steals),
;
    fun steal(player: Player) {
        if(!player.inventory.hasSpace){
            player.queue {
                messageBox("Your inventory is too full to hold any more.")
            }
            return
        }

        if (player.getSkills()[Skills.THIEVING].currentLevel < minStealLevel) {
            player.queue {
                messageBox("You need to be level $minStealLevel to steal from the ${stallID.getObjName(player.world.definitions, lowercase = true)}.")
            }
            return
        }

        if (attemptMsg != "") {
            val defaultMsg = "You attempt to steal #STEAL from the the #STALL.".replace("#STEAL", attemptMsg).replace("#STALL", stallID.getObjName(player.world.definitions, lowercase = true))
            player.message(defaultMsg)
        }

        val selector = Random.nextInt(0, steals.size)
        val item = steals.keys.toList()[selector]

        if (player.inventory.add(item).hasSucceeded()) {
            player.queue {
                player.lock()
                wait(1)
                player.animate(832)
                player.playSound(2581)

                val outMsg = "You steal #STEAL.".replace("#STEAL", steals[item] ?: "everything!")
                player.addXp(Skills.THIEVING, xpGain)

                player.world.queue {
                    val obj = player.getInteractingGameObj()
                    player.world.remove(obj)
                    player.message(outMsg)

                    val other = DynamicObject(obj, emptyStallId)
                    player.world.spawn(other)
                    wait(respawnCycles)
                    player.world.remove(other)
                    player.world.spawn(DynamicObject(obj))
                }
                player.unlock()
            }
        }
    }
}

 object StallRewards{
    val vegetable_stall_steals = mapOf(
        Items.ONION to "an onion",
        Items.CABBAGE to "a cabbage",
        Items.POTATO to "a potato",
        Items.TOMATO to "a tomato",
        Items.GARLIC to "a clove of garlic"
    )

    val bakers_stall_steals = mapOf(
        Items.CAKE to "a cake",
        Items.CHOCOLATE_SLICE to "a chocolate slice",
        Items.BREAD to "some bread"
    )

     val silk_stall_steals = mapOf(Items.SILK to "some silk")

     val fruit_stall_steals = mapOf(
         Items.COOKING_APPLE to "an apple",
         Items.BANANA to "a banana",
         Items.STRAWBERRY to "a strawberry",
         Items.REDBERRIES to "some redberries",
         Items.JANGERBERRIES to "some jangerberries",
         Items.STRANGE_FRUIT to "a strange fruit",
         Items.LIME to "a lime",
         Items.LEMON to "a lemon",
         Items.PINEAPPLE to "a pineapple",
         Items.PAPAYA_FRUIT to "a papaya fruit",
         Items.GOLOVANOVA_FRUIT_TOP to "a golovanova fruit top"
     )
}