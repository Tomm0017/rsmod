package gg.rsmod.plugins.content.skills.thieving.pickpocket

import gg.rsmod.plugins.content.combat.isAttacking
import gg.rsmod.plugins.content.combat.isBeingAttacked

//private val PICKPOCKET_ANIMATION = 881
//private val GLOVES_OF_SILENCE_BONUS = 5

PickpocketNpcs.values().forEach { npcType ->
    npcType.npcIds.forEach { npcId ->
        on_npc_option(npc = npcId, option = "pickpocket") {

            player.queue {
                val thievLvl: Int = player.getSkills().getCurrentLevel(Skills.THIEVING)
                val npcName = if(npcType.npcName == "") world.definitions.get(NpcDef::class.java, npcId).name else npcType.npcName
                if (thievLvl < npcType.lvl) {
                    player.message("You need level ${npcType.lvl} thieving to pick the $npcName's pocket.")
                    return@queue
                }
                if (player.isAttacking() || player.isBeingAttacked()) {
                    player.message("You can't pickpocket while in combat!")
                    return@queue
                }
                if (!player.inventory.hasSpace) {
                    player.message("You don't have enough inventory space to pickpocket!")
                    return@queue
                }

                //pickpocketing animation and starting message
                player.animate(881)
                player.message("You attempt to pickpocket the $npcName...")

                //wait 3 game cycles
                player.lock = LockState.FULL_WITH_ITEM_INTERACTION
                wait(3)
                player.lock = LockState.NONE

                //determine if the pickpocket was successful or not by "if random number is within success chances"
                val bonus = if (player.hasEquipped(EquipmentType.GLOVES, Items.GLOVES_OF_SILENCE)) 5 else 0
                if (thievLvl.interpolate(55, 95, npcType.lvl, 99, 100 - bonus)) {

                    player.message("...and you succeed!")
                    val reward = npcType.rewardSet.getRandom()
                    player.inventory.add(reward)
                    player.addXp(Skills.THIEVING, npcType.experience)

                } else {
                    //if failed, sends relevant messages
                    player.message("...and you have failed.")

                    //damages player for a value in the npc's damage range
                    player.hit(npcType.damage.random())

                    //stuns the player then waits til the stun is done to continue
                    player.stun(npcType.stunTicks)
                }
            }
        }
    }
}


