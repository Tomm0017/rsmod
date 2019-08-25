package gg.rsmod.plugins.content.npcs.npcInfo

arrayOf(Npcs.CHICKEN_1173, Npcs.CHICKEN_1174).forEach { chicken ->

    set_combat_def(chicken) {
        configs {
            attackSpeed = 4
            respawnDelay = 23
        }

        stats {
            hitpoints = 3
            attack = 1
            strength = 1
            defence = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            attackStab = 0
            attackSlash = 0
            attackCrush = 0
            attackMagic = 0
            attackRanged = 0

            defenceStab = -42
            defenceSlash = -42
            defenceCrush = -42
            defenceMagic = -42
            defenceRanged = -42

            attackBonus = -47
            strengthBonus = -42
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 5387
            block = 5388
            death = 5389
        }
    }

    set_drop_table(chicken) {
        droptable {
            dropTableJSON = "chickens/chicken.drops.json"
        }
    }
}