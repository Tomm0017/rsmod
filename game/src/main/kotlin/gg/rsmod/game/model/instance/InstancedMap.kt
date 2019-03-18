package gg.rsmod.game.model.instance

import gg.rsmod.game.model.Area

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class InstancedMap(val area: Area, val chunks: InstancedChunkSet)