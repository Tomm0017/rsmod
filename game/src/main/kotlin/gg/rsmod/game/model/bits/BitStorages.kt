package gg.rsmod.game.model.bits

/**
 * A decoupled file with [BitStorage]s that are used throughout the game module.
 *
 * @author Tom <rspsmods@gmail.com>
 */

val INFINITE_VARS_STORAGE = BitStorage(persistenceKey = "inf_vars")

enum class InfiniteVarsType(override val startBit: Int, override val endBit: Int = startBit) : StorageBits {
    RUN(startBit = 0),
    PRAY(startBit = 1),
    HP(startBit = 2)
}