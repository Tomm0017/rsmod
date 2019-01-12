package gg.rsmod.plugins.osrs.content.inter.options

import gg.rsmod.game.model.StorageBits

/**
 * @author Tom <rspsmods@gmail.com>
 */
enum class GameNotificationType(override val startBit: Int, override val endBit: Int) : StorageBits {
    DISABLE_YELL(startBit = 0, endBit = 1)
}