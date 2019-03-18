package gg.rsmod.plugins.api.dsl

import gg.rsmod.game.model.Tile
import gg.rsmod.game.model.instance.InstancedChunkSet

data class InstancedChunkCoords(val chunkX: Int, val chunkZ: Int, val height: Int = 0, val rot: Int = 0) {
    val tile: Tile get() = Tile(chunkX shl 3, chunkZ shl 3, height)
}

fun Tile.toInstancedChunk(rot: Int): InstancedChunkCoords = InstancedChunkCoords(x shr 3, z shr 3, height, rot)

operator fun InstancedChunkSet.Builder.set(chunkX: Int, chunkZ: Int, height: Int = 0, coords: InstancedChunkCoords) {
    set(chunkX, chunkZ, height, coords.rot, coords.tile)
}

object InstancedChunks {
    infix fun from(chunks: InstancedChunkSet.Builder): InstancedChunkSet = chunks.build()
}

fun instancedChunks(actions: InstancedChunkSet.Builder.() -> Unit): InstancedChunkSet.Builder {
    val builder = InstancedChunkSet.Builder()
    actions(builder)
    return builder
}
