package gg.rsmod.plugins.content.npcs.kbd

set_multi_combat_region(region = 9033)

spawn_npc(Npcs.KING_BLACK_DRAGON, x = 2274, z = 4698, walkRadius = 5)

set_combat_def(Npcs.KING_BLACK_DRAGON) {
    species {
        + NpcSpecies.DRAGON
        + NpcSpecies.BASIC_DRAGON
    }

    configs {
        attackSpeed = 3
        respawnDelay = 50
    }

    aggro {
        radius = 16
        searchDelay = 1
    }

    stats {
        hitpoints = 240
        attack = 240
        strength = 240
        defence = 240
        magic = 240
    }

    bonuses {
        defenceStab = 70
        defenceSlash = 90
        defenceCrush = 90
        defenceMagic = 80
        defenceRanged = 70
    }

    anims {
        block = 89
        death = 92
    }

    slayerData {
        xp = 258.0
    }
}