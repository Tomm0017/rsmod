package gg.rsmod.plugins.content.skills.thieving.pickpocketing

import gg.rsmod.plugins.content.combat.isAttacking
import gg.rsmod.plugins.content.combat.isBeingAttacked

//for each npc in the enum
PickpocketInfo.values().forEach { npcClass ->

    //for each npc id in the current enum
    npcClass.ids.forEach { npcID ->

        //Extra check to ensure that the npc is pickpocket-able
        if (world.definitions.get(NpcDef::class.java, npcID).options.contains("Pickpocket")) {

            //Registers what to do with pickpocketing option
            on_npc_option(npc = npcID, option = "pickpocket") {

                //accesses the player queue
                player.queue {
                    if (!player.isAttacking() && !player.isBeingAttacked()) {
                        if (player.inventory.hasSpace) {
                            //checks if the player has the required level for the npc
                            val thievLvl: Int = player.getSkills().getCurrentLevel(Skills.THIEVING)
                            if (thievLvl >= npcClass.lvl) {

                                //pickpocketing animation and starting message
                                player.animate(881)
                                player.message("You attempt to pickpocket the ${npcClass.npcName}...")

                                //wait 3 game cycles
                                player.lock = LockState.FULL_WITH_ITEM_INTERACTION
                                wait(3)
                                player.lock = LockState.NONE

                                //determine if the pickpocket was successful or not by "if random number is within success chances"
                                val cap = if (player.hasEquipped(EquipmentType.GLOVES, Items.GLOVES_OF_SILENCE)) 5 else 0
                                if (thievLvl.interpolate(55, 95, npcClass.lvl, 99, 100 - cap)) {

                                    player.message("...and you succeed!")
                                    val reward = npcClass.rewards.getRandom()
                                    player.inventory.add(reward)
                                    player.addXp(Skills.THIEVING, npcClass.exp)

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
                        } else {
                            player.message("You don't have enough inventory space to pickpocket!")
                        }
                    }else{
                        player.message("You can't pickpocket while in combat!")
                    }
                }
            }
        }
    }
}


