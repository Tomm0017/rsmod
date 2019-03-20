package gg.rsmod.game.model.instance

/**
 * An 8x8 tile area (aka, chunk) inside an instanced area.
 *
 * @param packed
 * A bit-packed value of the rotated, original [gg.rsmod.game.model.region.ChunkCoords]
 * (to copy).
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class InstancedChunk(var packed: Int) {

    val rot: Int get() = (packed shr 1) and 0x3
}