package gg.rsmod.plugins.content.npcs.npcInfo.rats

arrayOf(Npcs.GIANT_RAT_2863, Npcs.GIANT_RAT_2864).forEach { rats ->
    set_combat_def(rats) {
        configs {
            attackSpeed = 4
            respawnDelay = 27
        }

        stats {
            hitpoints = 10
            attack = 6
            strength = 5
            defence = 2
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

            attackBonus = 0
            strengthBonus = 0
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 4933
            block = 4934
            death = 4935
        }
    }

    set_drop_table(rats) {
        droptable {
            dropTableJSON = "rats/giant_rat_lvl6.drops.json"
        }
    }
}