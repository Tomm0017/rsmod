package gg.rsmod.plugins.content.npcs.npcInfo.hillgiants

arrayOf(Npcs.HILL_GIANT, Npcs.HILL_GIANT_2099, Npcs.HILL_GIANT_2100, Npcs.HILL_GIANT_2101, Npcs.HILL_GIANT_2102, Npcs.HILL_GIANT_2103, Npcs.HILL_GIANT_7261).forEach { Npc ->

    set_combat_def(Npc) {
        configs {
            attackSpeed = 4
            respawnDelay = 27
        }

        aggro {
            radius = 3
            searchDelay = 0
        }

        stats {
            hitpoints = 35
            attack = 18
            strength = 22
            defence = 26
            magic = 1
            ranged = 1
        }

        bonuses {
            attackStab = 0
            attackSlash = 0
            attackCrush = 0
            attackMagic = 0
            attackRanged = 0

            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 0
            defenceRanged = 0

            attackBonus = 18
            strengthBonus = 16
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 4652
            block = 4651
            death = 4653
        }
    }

    set_drop_table(Npc) {
        droptable {
            dropTableJSON = "hillgiants/hillgiant.drops.json"
        }
    }
}