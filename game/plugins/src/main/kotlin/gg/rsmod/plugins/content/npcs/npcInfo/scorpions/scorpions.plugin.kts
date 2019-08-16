package gg.rsmod.plugins.content.npcs.npcInfo.scorpions

arrayOf(Npcs.SCORPION_3024).forEach { scorpion ->
    set_combat_def(scorpion) {
        configs {
            attackSpeed = 6
            respawnDelay = 24
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 17
            attack = 11
            strength = 12
            defence = 11
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 5
            defenceSlash = 15
            defenceCrush = 15
            defenceMagic = 0
            defenceRanged = 5
        }

        anims {
            attack = 6254
            block = 6255
            death = 6256
        }
    }
}