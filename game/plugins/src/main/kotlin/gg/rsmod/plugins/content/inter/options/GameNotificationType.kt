package gg.rsmod.plugins.content.inter.options

import gg.rsmod.game.model.bits.StorageBits

enum class GameNotificationType(override val startBit: Int, override val endBit: Int = startBit) : StorageBits {
    DISABLE_YELL(startBit = 0)
}