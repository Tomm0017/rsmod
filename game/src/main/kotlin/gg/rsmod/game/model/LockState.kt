package gg.rsmod.game.model

/**
 * A [LockState] is a state in which an entity can be found in that will
 * prevent or allow them to perform certain actions.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class LockState {

    /**
     * Can freely move around, read and write [gg.rsmod.game.message.Message]s.
     */
    NONE,

    /**
     * Cannot log out or perform various actions such as handling incoming
     * [gg.rsmod.game.message.Message]s.
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

    fun canMove(): Boolean = when (this) {
        NONE -> true
        else -> false
    }
}