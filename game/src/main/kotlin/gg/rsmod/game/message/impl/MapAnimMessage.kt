package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author bmyte <bmytescape@gmail.com>
 */
data class MapAnimMessage(val id: Int, val height: Int, val delay: Int, val tile: Int) : Message