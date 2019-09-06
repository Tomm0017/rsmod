package gg.rsmod.plugins.content.areas.lumbridge.spawns

val SHEEP = intArrayOf(
        Npcs.SHEEP_2693,
        Npcs.SHEEP_2694,
        Npcs.SHEEP_2695,
        Npcs.SHEEP_2699,
        Npcs.SHEEP_2786,
        Npcs.SHEEP_2787
)

val LAMB = intArrayOf(
        Npcs.LAMB,
        Npcs.LIL_LAMB
)

val RAM = intArrayOf(
        Npcs.RAM_1262,
        Npcs.RAM_1263,
        Npcs.RAM_1264
)

val FAKE_SHEEP = intArrayOf(
        Npcs.SHEEP
)

val SPAWNS = arrayOf(
        Tile(3206,3270),
        Tile(3206,3260),
        Tile(3203,3266),
        Tile(3200,3273),
        Tile(3200,3266),
        Tile(3200,3259),
        Tile(3195,3260),
        Tile(3195,3266),
        Tile(3197,3273)
)

val sheep_spawns = 10
val ram_spawns = 4
val lamb_spawns = 2
val fake_sheep_spawns = 1

for(i in (1..sheep_spawns)) {
    val randSheep = (0..(SHEEP.size-1)).random()
    val randSpawn = (0..(SPAWNS.size-1)).random()

    spawn_npc(SHEEP[randSheep], SPAWNS[randSpawn].x, SPAWNS[randSpawn].z, 0, 5)
}

for(i in (1..ram_spawns)) {
    val rnpc = (0..(RAM.size-1)).random()
    val rspawn = (0..(SPAWNS.size-1)).random()

    spawn_npc(RAM[rnpc], SPAWNS[rspawn].x, SPAWNS[rspawn].z, 0, 5)
}

for(i in (0..(lamb_spawns-1))) {
    val rspawn = (0..(SPAWNS.size-1)).random()
    spawn_npc(LAMB[i], SPAWNS[rspawn].x, SPAWNS[rspawn].z, 0, 5)
}

val rspawn = (0..(SPAWNS.size-1)).random()
spawn_npc(FAKE_SHEEP[0], SPAWNS[rspawn].x, SPAWNS[rspawn].z, 0, 5)

