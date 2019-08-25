package gg.rsmod.plugins.content.npcs.npcInfo.farmers

arrayOf(Npcs.FARMER_3086, Npcs.FARMER_3087, Npcs.FARMER_3088).forEach { farmer ->

    set_combat_def(farmer) {
        configs {
            attackSpeed = 4
            respawnDelay = 23
        }

        stats {
            hitpoints = 12
            attack = 3
            strength = 4
            defence = 8
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

            attackBonus = 5
            strengthBonus = 6
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 433
            block = 434
            death = 836
        }
    }

    set_drop_table(farmer) {
        droptable {
            dropTableJSON = "farmers/farmer.drops.json"
        }
    }
}