package gg.rsmod.plugins.content.npcs.goblins

val Goblins = arrayOf(Npcs.GOBLIN_3028, Npcs.GOBLIN_3029, Npcs.GOBLIN_3030, Npcs.GOBLIN_3031,
        Npcs.GOBLIN_3032, Npcs.GOBLIN_3033, Npcs.GOBLIN_3034, Npcs.GOBLIN_3035, Npcs.GOBLIN_3036)

Goblins.forEach { goblins ->

    set_combat_def(goblins) {
        configs {
            attackSpeed = 6
            respawnDelay = 100
        }

        stats {
            hitpoints = 5
            attack = 1
            defence = 1
            strength = 1
            magic = 1
            ranged = 1
        }

        bonuses {
            attackStab = 0
            attackSlash = 0
            attackCrush = 0
            attackMagic = 0
            attackRanged = 0

            defenceStab = -15
            defenceSlash = -15
            defenceCrush = -15
            defenceMagic = -15
            defenceRanged = -15

            attackBonus = 0
            strengthBonus = 0
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            death = 6182
            block = 6183
            attack = 6184
        }
    }

    set_drop_table(goblins) {
        droptable {
            dropTableJSON = "goblins/goblin.drops.json"
        }
    }
}