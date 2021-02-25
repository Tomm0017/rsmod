package gg.rsmod.game.model.region

import gg.rsmod.game.model.Tile

class Region(val region: Int) {
    /**
     * bottom-left Tile in region
     */
    val baseCoord = Tile((region shr 8) shl 6, (region and 0xFF) shl 6, height = 0)

    val tiles = Array<Tile>(64 * 64){
        Tile(baseCoord.x + it%64, baseCoord.z + it/64, baseCoord.height)
    }
}