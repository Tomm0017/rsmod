package gg.rsmod.game.map

import gg.rsmod.game.model.Tile

/**
 * A [Region] is a collection of 8x8 [Chunk]s, which make it equal to a tile size
 * of 64x64.
 *
 * @param base
 * The bottom-left tile where the [Region] begins.
 *
 * @param chunks
 * The [Chunk]s that occupy this [Region]. The [Chunk]s must be in order from
 * bottom left to top right.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Region(val base: Tile, val chunks: Array<Chunk?>) {

    fun toInteger(): Int = (base.x shl 16) or base.z

    fun toTile(): Tile = base
}