package gg.rsmod.plugins.content.npcs.cows

arrayOf(Npcs.COW_2791, Npcs.COW_2793, Npcs.COW).forEach { cow ->
    set_combat_def(cow) {
        configs {
            attackSpeed = 4
            respawnDelay = 15
        }

        stats {
            hitpoints = 8
            attack = 1
            defence = 1
            strength = 1
            magic = 1
            ranged = 1
        }

        anims {
            attack = 5849
            block = 5850
            death = 5851
        }

        bonuses {
            attackStab = 0
            attackSlash = 0
            attackCrush = 0
            attackMagic = 0
            attackRanged = 0

            defenceStab = -21
            defenceSlash = -21
            defenceCrush = -21
            defenceMagic = -21
            defenceRanged = -21

            attackBonus = 0
            strengthBonus = 0
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }
    }

    set_drop_table(cow) {
        droptable {
            dropTableJSON = "cows/cow.drops.json"
        }
    }
}