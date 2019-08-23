package gg.rsmod.plugins.content.npcs.npcInfo.skeletons

arrayOf(Npcs.SKELETON, Npcs.SKELETON_71, Npcs.SKELETON_72, Npcs.SKELETON_73).forEach { skeleton ->
    set_combat_def(skeleton) {
        configs {
            attackSpeed = 4
            respawnDelay = 66
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 29
            attack = 15
            strength = 18
            defence = 17
            magic = 1
            ranged = 1
        }

        bonuses {
            defenceStab = 5
            defenceSlash = 5
            defenceCrush = -5
            defenceMagic = 0
            defenceRanged = 5

            attackBonus = 0
            strengthBonus = 0
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
            dropTableJSON = "skeletons/skeleton.drops.json"
        }
    }
}