package gg.rsmod.plugins.content.npcs.npcInfo.skeletons

arrayOf(Npcs.SKELETON_77, Npcs.SKELETON_78, Npcs.SKELETON_81).forEach { skeleton ->
    set_combat_def(skeleton) {
        configs {
            attackSpeed = 4
            respawnDelay = 56
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 17
            attack = 24
            strength = 24
            defence = 24
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 9
            defenceSlash = 11
            defenceCrush = -2
            defenceMagic = 1
            defenceRanged = 4

            attackBonus = 15
            strengthBonus = 14
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 5485
            block = 5489
            death = 5491
        }
    }

    set_drop_table(skeleton) {
        droptable {
            dropTableJSON = "skeletons/skeleton_lvl25.drops.json"
        }
    }
}

arrayOf(Npcs.SKELETON_79, Npcs.SKELETON_80).forEach { skeletons ->
    set_combat_def(skeletons) {
        configs {
            attackSpeed = 4
            respawnDelay = 56
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 17
            attack = 24
            strength = 24
            defence = 24
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 9
            defenceSlash = 11
            defenceCrush = -2
            defenceMagic = 1
            defenceRanged = 4

            attackBonus = 15
            strengthBonus = 14
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 5487
            block = 5489
            death = 5491
        }
    }

    set_drop_table(skeletons) {
        droptable {
            dropTableJSON = "skeletons/skeleton_lvl25.drops.json"
        }
    }
}
