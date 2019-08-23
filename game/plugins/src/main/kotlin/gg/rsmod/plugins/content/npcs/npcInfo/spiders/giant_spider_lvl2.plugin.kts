package gg.rsmod.plugins.content.npcs.npcInfo.spiders

arrayOf(Npcs.GIANT_SPIDER_3017).forEach { spider ->
    set_combat_def(spider) {
        configs {
            attackSpeed = 4
            respawnDelay = 27
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 5
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = -10
            defenceSlash = -10
            defenceCrush = -10
            defenceMagic = -10
            defenceRanged = -10

            attackBonus = -10
            strengthBonus = -10
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 5327
            block = 5328
            death = 5329
        }
    }
}