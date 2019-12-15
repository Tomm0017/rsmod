package gg.rsmod.plugins.content.skills.woodcutting

import gg.rsmod.plugins.api.cfg.Npcs


private val teaseableNpcs = arrayOf(Npcs.SPINED_LARUPIA,Npcs.HORNED_GRAAHK)

teaseableNpcs.forEach { npcId ->
    on_npc_option(npc = npcId, option = "tease") {
        val npc = player.getInteractingNpc()

        player.resetFacePawn()
        player.faceTile(npc.tile)
        if (!player.inventory.contains(Items.TEASING_STICK)) {
            player.message("You need a teasing stick to do this.")
            return@on_npc_option
        }
        player.queue {
            player.animate(893) //tease anim
            val npc = player.getInteractingNpc()
            npc.attack(player)}
    }
}
