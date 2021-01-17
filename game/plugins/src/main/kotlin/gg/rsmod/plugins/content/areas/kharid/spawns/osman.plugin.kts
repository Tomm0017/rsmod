package gg.rsmod.plugins.content.areas.kharid.spawns

spawn_npc(Npcs.OSMAN_1809, 3286, 3180)

on_npc_option(Npcs.OSMAN_1809, "Talk-to"){
    player.queue {
        when(options("I'd like to talk about sq'irks.")){
            1 -> {
                chatPlayer("I'd like to talk about sq'irks.")
                chatPlayer("I have some sq'irk juice for you.")
                val winterCount = player.inventory.getItemCount(Items.WINTER_SQIRKJUICE)
                val springCount = player.inventory.getItemCount(Items.SPRING_SQIRKJUICE)
                val autumnCount = player.inventory.getItemCount(Items.AUTUMN_SQIRKJUICE)
                val summerCount = player.inventory.getItemCount(Items.SUMMER_SQIRKJUICE)
                player.inventory.remove(Items.WINTER_SQIRKJUICE, winterCount)
                player.inventory.remove(Items.SPRING_SQIRKJUICE, springCount)
                player.inventory.remove(Items.AUTUMN_SQIRKJUICE, autumnCount)
                player.inventory.remove(Items.SUMMER_SQIRKJUICE, summerCount)
                val totXp: Double = 0.0 + (350*winterCount) + (1350*springCount) + (2350*autumnCount) + (3000*summerCount)
                itemMessageBox("Osman imparts some Thieving advice to you<br>" +
                        " ( $totXp Thieving experience points )<br>" +
                        " as a reward for the sq'irk juice.", 2631, 300)
            }
        }
    }
}