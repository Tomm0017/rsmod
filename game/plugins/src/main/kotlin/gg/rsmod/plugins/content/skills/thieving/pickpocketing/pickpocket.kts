package gg.rsmod.plugins.content.skills.thieving.pickpocketing

import kotlin.random.Random

//for each npc in the enum
PickpocketInfo.values().forEach { npcClass ->
    //for each npc id in the current enum
    npcClass.ids.forEach {npcID ->
        //adds interaction for pickpocketing
        on_npc_option(npc = npcID, option = "pickpocket") {
            //accesses the player queue
            player.queue {
                //checks if the player has the required level for the npc
                val thievLvl: Int = player.getSkills().getCurrentLevel(Skills.THIEVING)
                if (thievLvl >= npcClass.lvl) {
                    //pickpocketing animation and starting message
                    player.animate(881)
                    player.message("You attempt to pickpocket the ${npcClass.npcName}...")

                    //wait 3 game cycles
                    wait(3)

                    //determine if the pickpocket was successful or not by "if random number is within success chances" 
                    if(thievLvl.interpolate(55,95,npcClass.lvl,99,100) ) {
                        player.message("...and you succeed!")
                        val rand = Random
                        val reward = npcClass.rewards.getRandomNode(rand).value
                        player.inventory.add(reward)
                        player.addXp(Skills.THIEVING, npcClass.exp)
                    //if failed to pickpocket
                    } else {
                        //if failed, sends relevant messages
                        player.message("...and you have failed.")

                        //damages player for a value in the npc's damage range
                        player.hit(npcClass.damage.random(), HitType.HIT)

                        //stuns the player then waits til the stun is done to continue
                        player.stun(npcClass.stunTicks)
                    }
                } else {
                    player.message("You need level ${npcClass.lvl} to pickpocket a ${npcClass.npcName}.")
                }
            }
        }
    }
}


