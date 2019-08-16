package gg.rsmod.plugins.content.npcs.npcInfo.rats

arrayOf(Npcs.RAT_2854).forEach { rats ->
    set_combat_def(rats) {
        configs {
            attackSpeed = 4
            respawnDelay = 5
        }

        stats {
            hitpoints = 2
            attack = 1
            defence = 0
            strength = 0
            magic = 0
            ranged = 0
        }

        anims {
            attack = 2705
            block = 2706
            death = 2707
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
    }

    set_drop_table(rats) {
        droptable {
            dropTableJSON = "rats/rat.drops.json"
        }
    }
}