package gg.rsmod.plugins.content.npcs.cows

arrayOf(Npcs.COW_CALF, Npcs.COW_CALF_2794, Npcs.COW_CALF_2801).forEach { cowcalf ->

    set_combat_def(cowcalf) {
        configs {
            attackSpeed = 6
            respawnDelay = 15
        }

        stats {
            hitpoints = 6
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

            defenceStab = -26
            defenceSlash = -26
            defenceCrush = -26
            defenceMagic = -26
            defenceRanged = -26

            attackBonus = 0
            strengthBonus = 0
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }
    }

    set_drop_table(cowcalf) {
        droptable {
            dropTableJSON = "cows/cowcalf.drops.json"
        }
    }
}