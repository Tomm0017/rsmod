package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ChangeStaticRegionMessage(val x: Int, val z: Int, val regions: Int, val xteas: ByteArray) : Message