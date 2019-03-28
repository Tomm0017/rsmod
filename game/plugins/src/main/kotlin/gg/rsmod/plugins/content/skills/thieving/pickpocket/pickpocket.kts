package gg.rsmod.plugins.content.skills.thieving.pickpocketing

import gg.rsmod.plugins.content.combat.isAttacking
import gg.rsmod.plugins.content.combat.isBeingAttacked
import gg.rsmod.plugins.content.skills.thieving.pickpocket.PickpocketNpc

private val PICKPOCKET_ANIMATION = 881
private val GLOVES_OF_SILENCE_BONUS = 5

PickpocketNpc.values.forEach { pickpocketNpc ->
    pickpocketNpc.npcIds.forEach { npcId ->
        on_npc_option(npc = npcId, option = "pickpocket") {
            player.queue {
                pickpocket(npcId, pickpocketNpc)
            }
        }
    }
}

suspend fun QueueTask.pickpocket(npcId: Int, npc: PickpocketNpc) {
    val thievLvl: Int = player.getSkills().getCurrentLevel(Skills.THIEVING)
    val npcName = npc.npcName ?: world.definitions.get(NpcDef::class.java, npcId).name
    if (thievLvl < npc.reqLevel) {
        player.message("You need level ${npc.reqLevel} thieving to pick the $npcName's pocket.")
        return
    }
    if (player.isAttacking() || player.isBeingAttacked()) {
        player.message("You can't pickpocket while in combat!")
        return
    }
    if (!player.inventory.hasSpace) {
        player.message("You don't have enough inventory space to pickpocket!")
        return
    }

    //pickpocketing animation and starting message
    player.animate(PICKPOCKET_ANIMATION)
    player.message("You attempt to pickpocket the $npcName...")

    //wait 3 game cycles
    player.lock = LockState.FULL_WITH_ITEM_INTERACTION
    wait(3)
    player.lock = LockState.NONE

    //determine if the pickpocket was successful or not by "if random number is within success chances"
    val bonus = if (player.hasEquipped(EquipmentType.GLOVES, Items.GLOVES_OF_SILENCE)) GLOVES_OF_SILENCE_BONUS else 0
    if (thievLvl.interpolate(55, 95, npc.reqLevel, 99, 100 - bonus)) {

        player.message("...and you succeed!")
        val reward = npc.rewardSet.getRandom()
        player.inventory.add(reward)
        player.addXp(Skills.THIEVING, npc.experience)

    } else {
        //if failed, sends relevant messages
        player.message("...and you have failed.")

        //damages player for a value in the npc's damage range
        player.hit(npc.damage.random())

        //stuns the player then waits til the stun is done to continue
        player.stun(npc.stunTicks)
    }
}