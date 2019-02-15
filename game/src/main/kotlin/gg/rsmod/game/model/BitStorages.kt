package gg.rsmod.game.model

/**
 * @author Tom <rspsmods@gmail.com>
 */
val INFINITE_VARS_STORAGE = BitStorage(persistenceKey = "inf_vars")

enum class InfiniteVarsType(override val startBit: Int, override val endBit: Int) : StorageBits {
    RUN(startBit = 0, endBit = 1),
    PRAY(startBit = 2, endBit = 3),
    HP(startBit = 4, endBit = 5)
}