package gg.rsmod.game.map

import gg.rsmod.game.model.Tile

/**
 * A [Chunk] is an 8x8 tile area and should not overlap with any other [Chunk].
 *
 * @param base
 * The [Tile] which this chunk starts on.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Chunk(val base: Tile) {

    fun toTile(): Tile = base
}