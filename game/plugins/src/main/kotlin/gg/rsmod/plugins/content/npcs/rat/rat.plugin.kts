package gg.rsmod.plugins.content.npcs.rat

import java.io.InputStream

arrayOf(
        Npcs.RAT,
        Npcs.RAT_1021,
        Npcs.RAT_1022,
        Npcs.RAT_2492,
        Npcs.RAT_2513,
        Npcs.RAT_2854,
        Npcs.RAT_2855
).forEach { npc ->
    set_combat_def(npc) {
        configs {
            attackSpeed = 4
            respawnDelay = 60
        }

        stats {
            hitpoints = 2
            defence = 0
            attack = 1
        }

        anims {
            attack = 2706
            block = 2705
            death = 2707
        }
    }
}