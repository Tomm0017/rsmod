package gg.rsmod.plugins.content.npcs.npcInfo

/*
arrayOf(Npc).forEach { Npc ->

    set_combat_def(Npc) {
        configs {
            attackSpeed = 0
            respawnDelay = 0
            poisonChance = 0.0
            venomChance = 0.0
        }

        species {
            + NpcSpecies.Type
        }

        aggro {
            radius = 0
            searchDelay = 0
            aggroMinutes = 0
        }

        stats {
            hitpoints = 0
            attack = 0
            strength = 0
            defence = 0
            magic = 0
            ranged = 0
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
            attack = 0
            block = 0
            death = 0
        }

        slayerData {
            xp = 0.0
        }
    }

    set_drop_table(Npc) {
        droptable {
            dropTableJSON = "Npc/Npc.drops.json"
        }
    }
}