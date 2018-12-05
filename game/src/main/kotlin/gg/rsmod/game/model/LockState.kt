package gg.rsmod.game.model

/**
 * A [LockState] determines what a [Pawn] can and can't perform.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class LockState {

    /**
     * Can freely move around, read and write [gg.rsmod.net.message.Message]s.
     */
    NONE,

    /**
     * Cannot log out or perform various actions such as handling incoming
     * [gg.rsmod.net.message.Message]s.
     */
    FULL,

    /**
     * Similar to [FULL], but can log out.
     */
    FULL_WITH_LOGOUT;

    fun canLogout(): Boolean = when (this) {
        NONE, FULL_WITH_LOGOUT -> true
        else -> false
    }
}