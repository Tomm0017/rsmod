package gg.rsmod.plugins.content.npcs.npcInfo.hobgoblins

    set_combat_def(Npcs.HOBGOBLIN_3049) {
        configs {
            attackSpeed = 6
            respawnDelay = 34
        }

        aggro {
            radius = 3
            searchDelay = 0
        }

        stats {
            hitpoints = 29
            attack = 22
            strength = 24
            defence = 24
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

            attackBonus = 0
            strengthBonus = 0
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }

        anims {
            attack = 164
            block = 165
            death = 167
        }
    }

    set_drop_table(Npcs.HOBGOBLIN_3049) {
        droptable {
            dropTableJSON = "hobgoblins/hobgoblin.drops.json"
        }
    }