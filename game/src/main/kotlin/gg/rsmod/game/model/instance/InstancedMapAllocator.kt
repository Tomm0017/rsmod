package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area
import gg.rsmod.game.model.World
import gg.rsmod.game.model.region.Chunk

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InstancedMapAllocator {

    companion object {

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

    fun allocate(chunks: InstancedChunkSet): InstancedMap? {
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
    }

    fun removeCollision(map: InstancedMap) {

    }
}