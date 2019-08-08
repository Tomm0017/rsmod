package gg.rsmod.plugins.content.areas.lumbridge.spawns

// Western goblins behind general store

val GOBLINS = intArrayOf(
        Npcs.GOBLIN_3029,
        Npcs.GOBLIN_3030,
        Npcs.GOBLIN_3031,
        Npcs.GOBLIN_3032,
        Npcs.GOBLIN_3033,
        Npcs.GOBLIN_3034,
        Npcs.GOBLIN_3035,
        Npcs.GOBLIN_3036
)

val GOBLIN_SPAWNS = arrayOf(
        Tile(3201,3249),
        Tile(3205,3243),
        Tile(3199,3243),
        Tile(3196,3240),
        Tile(3190,3245),
        Tile(3190,3252),
        Tile(3181,3247),
        Tile(3187,3238)
)

val spawnCount = 15

for(i in (1..spawnCount)) {
    val rand = (0..(GOBLINS.size-1)).random()
    val randSpawn = (0..(GOBLIN_SPAWNS.size-1)).random()
    spawn_npc(GOBLINS[rand], GOBLIN_SPAWNS[randSpawn].x, GOBLIN_SPAWNS[randSpawn].z, 0, 8)
}