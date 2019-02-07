package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.service.xtea.XteaKeyService

/**
 * @author Tom <rspsmods@gmail.com>
 */
class RebuildNormalMessage(val x: Int, val z: Int, val xteaKeyService: XteaKeyService?) : Message