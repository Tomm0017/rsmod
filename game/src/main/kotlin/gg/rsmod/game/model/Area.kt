package gg.rsmod.game.model

import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class Area(val bottomLeftX: Int, val bottomLeftZ: Int, val topRightX: Int, val topRightZ: Int) {

    /**
     * Calculates the 'middle' tile of the area. The result is just an estimate
     * of what the middle tile should be, as it's possible for the area to not
     * be even in tiles.
     *
     * Example of when the tile is not perfectly centered:
     * [topRightX - bottomLeftZ % 2 != 0] or [topRightZ - bottomLeft % 2 != 0]
     */
    fun getMiddleTileEstimate(): Tile = Tile(bottomLeftX + (topRightX - bottomLeftX), bottomLeftZ + (topRightZ - bottomLeftZ))

    fun toBottomLeft(): Tile = Tile(bottomLeftX, bottomLeftZ)

    fun toTopRight(): Tile = Tile(topRightX, topRightZ)

    fun contains(t: Tile): Boolean = t.x in bottomLeftX..topRightX && t.z in bottomLeftZ..topRightZ

    fun contains(pawn: Pawn): Boolean = contains(pawn.tile)
}