package gg.rsmod.plugins.content.skills.thieving

import kotlin.random.Random



NpcTargetType.values().forEach { npcClass ->
    npcClass.ids.forEach {npcID ->
        on_npc_option(npc = npcID, option = "pickpocket") {
            player.queue {
                if (player.getSkills().getCurrentLevel(Skills.THIEVING) >= npcClass.lvl) {
                    player.animate(881)
                    player.message("You attempt to pick the ${npcClass.npcName}'s pocket.")

                    wait(3)

                    if (player.world.random(100) <= successChance(player.getSkills().getCurrentLevel(Skills.THIEVING), npcClass.lvl)) {

                        player.message("You steal ${npcClass.rewards.size}  ${npcClass.rewards.random()}")
                        val reward = npcClass.rewards[Random.nextInt(npcClass.rewards.size)]
                        player.inventory.add(reward)
                        player.addXp(Skills.THIEVING, npcClass.exp)
                    } else {
                        player.message("You have been stunned.")
                        player.hit(npcClass.damage.random(), HitType.HIT)
                        //player.
                    }
                } else {
                    player.message("You need a Thieving level of ${npcClass.lvl} to pickpocket a ${npcClass.npcName}.")
                }
            }
        }
    }
}
