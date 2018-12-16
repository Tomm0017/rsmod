package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginRegionMessage(val chunkX: Int, val chunkZ: Int, val playerIndex: Int, val tile: Tile) : Message