package gg.rsmod.game.region

import gg.rsmod.game.map.Map
import gg.rsmod.game.model.Tile
import org.junit.Test

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RegionTest {

    private val regions = Map()

    @Test
    fun surroundingsNoPriority() {
        val radius = 0

        val tile = Tile(radius * 8, radius * 8)
        val surrounding = regions.getSurroundingChunks(tile, radius, activeOnly = false,
                centerFirst = true, prioritized = false)

        println("Surrounding chunks from $tile with a chunk-radius of $radius")
        surrounding.forEach { chunk ->
            var chunkTile = chunk.toTile()
            chunkTile = Tile(chunkTile.x / 8, chunkTile.z / 8)
            println("\tChunk: $chunkTile")
        }
    }

    @Test
    fun surroundingWithPriority() {
        val radius = 0

        val tile = Tile(radius * 8, radius * 8)
        val surrounding = regions.getSurroundingChunks(tile, radius, activeOnly = false,
                centerFirst = true, prioritized = true)

        println("Prioritized surrounding chunks from $tile with a chunk-radius of $radius")
        surrounding.forEach { chunk ->
            var chunkTile = chunk.toTile()
            chunkTile = Tile(chunkTile.x / 8, chunkTile.z / 8)
            println("\tChunk: $chunkTile")
        }
    }
}