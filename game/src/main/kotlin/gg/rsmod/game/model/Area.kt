package gg.rsmod.game.model

/**
 * Represents a quad area in the world.
 *
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
    val centre: Tile get() = Tile(bottomLeftX + (topRightX - bottomLeftX), bottomLeftZ + (topRightZ - bottomLeftZ))

    val bottomLeft: Tile get() = Tile(bottomLeftX, bottomLeftZ)

    val topRight: Tile get() = Tile(topRightX, topRightZ)

    fun contains(x: Int, z: Int): Boolean = x in bottomLeftX..topRightX && z in bottomLeftZ..topRightZ

    fun contains(t: Tile): Boolean = t.x in bottomLeftX..topRightX && t.z in bottomLeftZ..topRightZ
}