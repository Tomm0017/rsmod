package gg.rsmod.game.model.path

import gg.rsmod.game.model.Tile
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PathRequest {

    var discard = false

    lateinit var path: Queue<Tile>
}