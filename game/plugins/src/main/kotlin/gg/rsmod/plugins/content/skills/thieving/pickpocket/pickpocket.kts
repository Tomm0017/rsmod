package gg.rsmod.plugins.content.skills.thieving.pickpocket

import gg.rsmod.plugins.content.combat.isAttacking
import gg.rsmod.plugins.content.combat.isBeingAttacked

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
    val playerThievingLvl = player.getSkills().getCurrentLevel(Skills.THIEVING)
    val npcName = npc.npcName ?: world.definitions.get(NpcDef::class.java, npcId).name
    if (playerThievingLvl < npc.reqLevel) {
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

    if (getPickpocketSuccess(playerThievingLvl, npc, player)) {

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

fun getPickpocketSuccess(playerThievingLvl: Int, npc: PickpocketNpc, player: Player): Boolean{

    //gets whether the player has Gloves of Silence equipped, and gives the 5% bonus if they do
    val bonus = if (player.hasEquipped(EquipmentType.GLOVES, Items.GLOVES_OF_SILENCE)) GLOVES_OF_SILENCE_BONUS else 0

    //applies the interpolate function. selects a chance from 55% to 95% based on thieving level
    return playerThievingLvl.interpolate(55, 95, npc.reqLevel, 99, 100 - bonus)
}