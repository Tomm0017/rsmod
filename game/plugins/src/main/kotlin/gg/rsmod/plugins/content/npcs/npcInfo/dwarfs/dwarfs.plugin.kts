package gg.rsmod.plugins.content.npcs.npcInfo.dwarfs

arrayOf(Npcs.DWARF_290).forEach { rats ->
    set_combat_def(rats) {
        configs {
            attackSpeed = 5
            respawnDelay = 24
        }

        stats {
            hitpoints = 16
            attack = 8
            defence = 8
            strength = 6
            magic = 1
            ranged = 1
        }

        anims {
            attack = 99
            block = 100
            death = 102
        }

        bonuses {
            attackStab = 5
            attackSlash = 0
            attackCrush = 0
            attackMagic = 0
            attackRanged = 0

            defenceStab = 0
            defenceSlash = 0
            defenceCrush = 0
            defenceMagic = 5
            defenceRanged = 0

            attackBonus = 5
            strengthBonus = 7
            rangedStrengthBonus = 0
            magicDamageBonus = 0
        }
    }

    set_drop_table(rats) {
        droptable {
            dropTableJSON = "dwarfs/dwarf.drops.json"
        }
    }
}