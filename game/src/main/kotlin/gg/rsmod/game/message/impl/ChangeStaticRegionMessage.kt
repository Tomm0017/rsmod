package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.model.Tile

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChangeStaticRegionMessage(val chunkX: Int, val chunkZ: Int, val tile: Tile) : Message