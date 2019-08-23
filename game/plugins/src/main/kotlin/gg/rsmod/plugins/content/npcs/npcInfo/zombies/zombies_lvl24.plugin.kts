package gg.rsmod.plugins.content.npcs.npcInfo.zombies

arrayOf(Npcs.ZOMBIE_55, Npcs.ZOMBIE_57).forEach { zombie ->
    set_combat_def(zombie) {
        configs {
            attackSpeed = 4
            respawnDelay = 23
        }

        species {
            +NpcSpecies.UNDEAD
        }

        aggro {
            radius = 3
            searchDelay = 1
        }

        stats {
            hitpoints = 30
            attack = 19
            strength = 21
            defence = 16
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
            attack = 5571
            block = 5574
            death = 5575
        }

        set_drop_table(zombie) {
            droptable {
                dropTableJSON = "zombies/zombie.drops.json"
            }
        }
    }
}

set_combat_def(Npcs.ZOMBIE_56) {
    configs {
        attackSpeed = 4
        respawnDelay = 23
    }

    species {
        + NpcSpecies.UNDEAD
    }

    aggro {
        radius = 3
        searchDelay = 1
    }

    stats {
        hitpoints = 30
        attack = 19
        strength = 21
        defence = 16
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
        attack = 5568
        block = 5567
        death = 5569
    }

    set_drop_table(Npcs.ZOMBIE_56) {
        droptable {
            dropTableJSON = "zombies/zombie.drops.json"
        }
    }
}

set_combat_def(Npcs.ZOMBIE_58) {
    configs {
        attackSpeed = 4
        respawnDelay = 23
    }

    species {
        + NpcSpecies.UNDEAD
    }

    aggro {
        radius = 3
        searchDelay = 1
    }

    stats {
        hitpoints = 30
        attack = 19
        strength = 21
        defence = 16
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
        attack = 5578
        block = 5579
        death = 5580
    }

    set_drop_table(Npcs.ZOMBIE_58) {
        droptable {
            dropTableJSON = "zombies/zombie.drops.json"
        }
    }
}