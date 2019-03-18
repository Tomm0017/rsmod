package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.World
import gg.rsmod.game.model.entity.DynamicObject
import gg.rsmod.game.model.entity.StaticObject
import gg.rsmod.game.model.region.Chunk
import mu.KotlinLogging

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMapAllocator {

    companion object {

        private val logger = KotlinLogging.logger {  }

        /**
         * 07 identifies instanced maps by having an X-axis of 6400 or above. They
         * use this for some client scripts, such as a Theatre of Blood cs2 that
         * will change depending on if you're inside the Theatre (in an instance)
         * or in the lobby.
         *
         * Current stats (as of 17/03/2018):
         * Area size: 3200x6400
         * Instance support: 5000 [50 in x-axis * 100 in z-axis]
         *
         * [Instance support]: the amount of instances the world can support at a time,
         * assuming every map is 64x64 in size, which isn't always the case.
         */
        private val VALID_AREA = Area(6400, 128, 9600, 6400)//Area(bottomLeftX = 6400, bottomLeftZ = 0, topRightX = 9600, topRightZ = 6400)
    }

    private val maps = arrayListOf<InstancedMap>()

    fun allocate(world: World, chunks: InstancedChunkSet): InstancedMap? {
        val area = VALID_AREA
        val step = 64

        /**
         * The total amount of tiles that the new [InstancedMap] will occupy.
         */
        val totalTiles = chunks.regionSize * Chunk.REGION_SIZE

        for (x in area.bottomLeftX until area.topRightX step step) {
            for (z in area.bottomLeftZ until area.topRightZ step step) {

                /**
                 * If a map is already allocated in [x,z], we move on.
                 */
                if (maps.any { it.area.contains(x, z) || it.area.contains(x + totalTiles - 1, z + totalTiles - 1) }) {
                    continue
                }

                val map = allocate(x, z, chunks)
                applyCollision(world, map)
                maps.add(map)
                return map
            }
        }

        return null
    }

    private fun allocate(x: Int, z: Int, chunks: InstancedChunkSet): InstancedMap =
            InstancedMap(Area(x, z, x + chunks.regionSize * Chunk.REGION_SIZE, z + chunks.regionSize * Chunk.REGION_SIZE), chunks)

    private fun deallocate(map: InstancedMap) {
        if (maps.remove(map)) {
            removeCollision(map)
        }
    }

    fun applyCollision(world: World, map: InstancedMap) {
        val chunks = map.chunks.values

        val localWidth = Chunk.CHUNK_SIZE - 1
        val localHeight = Chunk.CHUNK_SIZE - 1

        chunks.forEach { chunkCoordinates, chunk ->
            val copyTile = Tile.fromRotatedHash(chunk.packed)

            val chunkH = (chunkCoordinates shr 28) and 0x3
            val chunkX = (chunkCoordinates shr 14) and 0x3FF
            val chunkZ = chunkCoordinates and 0x7FF

            val newTile = map.area.bottomLeft.transform((chunkX - 6) shl 3, (chunkZ - 6) shl 3, chunkH)

            val copyChunk = world.chunks.get(copyTile.chunkCoords, createIfNeeded = true)!!
            val newChunk = world.chunks.getOrCreate(newTile)

            copyChunk.getEntities<StaticObject>(EntityType.STATIC_OBJECT).forEach { obj ->
                if (obj.tile.height == chunkH && obj.tile.isInSameChunk(copyTile)) {
                    val def = obj.getDef(world.definitions)
                    var width = def.width
                    var length = def.length

                    val diffX = obj.tile.x - ((obj.tile.x shr 3) shl 3)
                    val diffZ = obj.tile.z - ((obj.tile.z shr 3) shl 3)

                    val newRot = (obj.rot + chunk.rot) and 0x3
                    if ((obj.rot and 0x1) == 1) {
                        width = def.length
                        length = def.width
                    }

                    val localX: Int
                    val localZ: Int
                    when (chunk.rot) {
                        1 -> {
                            localX = diffZ
                            localZ = localHeight - diffX - (width - 1)
                        }
                        2 -> {
                            localX = localWidth - diffX - (width - 1)
                            localZ = localHeight - diffZ - (length - 1)
                        }
                        3 -> {
                            localX = localWidth - diffZ - (length - 1)
                            localZ = diffX
                        }
                        else -> {
                            localX = diffX
                            localZ = diffZ
                        }
                    }

                    val newObj = DynamicObject(obj.id, obj.type, newRot, newTile.transform(localX, localZ))
                    if (newObj.tile.isInSameChunk(newTile)) {
                        newChunk.addEntity(world, newObj, newObj.tile)
                    } else {
                        logger.warn { "Could not copy object due to its size and rotation outcome (object rotation + chunk rotation). " +
                                "The object would, otherwise, be spawned out of bounds of its original chunk. [obj=$obj, copy=$newObj]" }
                    }
                }
            }
        }
    }

    fun removeCollision(map: InstancedMap) {

    }
}