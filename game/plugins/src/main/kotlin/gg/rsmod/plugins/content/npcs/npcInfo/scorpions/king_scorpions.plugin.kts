package gg.rsmod.plugins.content.npcs.npcInfo.scorpions

arrayOf(Npcs.KING_SCORPION).forEach { scorpion ->
    set_combat_def(scorpion) {
        configs {
            attackSpeed = 4
            respawnDelay = 24
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 30
            attack = 30
            strength = 29
            defence = 23
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