package gg.rsmod.plugins.content.npcs.mossgiants

arrayOf(Npcs.MOSS_GIANT_2090, Npcs.MOSS_GIANT_2091, Npcs.MOSS_GIANT_2092, Npcs.MOSS_GIANT_2093).forEach { mossgiant ->
    set_combat_def(mossgiant) {
        configs {
            attackSpeed = 4
            respawnDelay = 28
        }

        stats {
            hitpoints = 60
            attack = 30
            defence = 30
            strength = 30
            magic = 1
            ranged = 1
        }

        anims {
            attack = 4658
            block = 4657
            death = 4659
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

            attackBonus = 33
            strengthBonus = 31
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }
    }

    set_drop_table(mossgiant) {
        droptable {
            dropTableJSON = "mossgiants/mossgiant.drops.json"
        }
    }
}