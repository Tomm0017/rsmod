package gg.rsmod.plugins.content.npcs.rockcrab

arrayOf(Npcs.ROCK_CRAB, Npcs.ROCK_CRAB_102).forEach { npc ->
    set_combat_def(npc) {
        configs {
            attackSpeed = 4
            respawnDelay = 50
        }

        aggro {
            radius = 3
            searchDelay = 1
            aggroMinutes = 10
        }

        stats {
            hitpoints = 50
        }

        anims {
            attack = 1312
            block = 1313
            death = 1314
        }
    }
}