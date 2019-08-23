package gg.rsmod.plugins.content.npcs.npcInfo.skeletons

arrayOf(Npcs.SKELETON_74, Npcs.SKELETON_75, Npcs.SKELETON_76).forEach { skeleton ->
    set_combat_def(skeleton) {
        configs {
            attackSpeed = 4
            respawnDelay = 34
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 24
            attack = 17
            strength = 17
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