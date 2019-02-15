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
     * Delays actions such as turning prayers on and being damaged.
     */
    DELAY_ACTIONS,

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

    fun canAttack(): Boolean = when (this) {
        NONE -> true
        else -> false
    }

    fun canDropItems(): Boolean = when (this) {
        NONE -> true
        else -> false
    }

    fun canNpcInteract(): Boolean = when (this) {
        NONE -> true
        else -> false
    }

    fun canItemInteract(): Boolean = when (this) {
        NONE -> true
        else -> false
    }

    fun canComponentInteract(): Boolean = when (this) {
        NONE -> true
        else -> false
    }

    fun canUsePrayer(): Boolean = when (this) {
        NONE, DELAY_ACTIONS -> true
        else -> false
    }

    fun canRestoreRunEnergy(): Boolean = when (this) {
        DELAY_ACTIONS -> false
        else -> true
    }

    fun delaysPrayer(): Boolean = when (this) {
        DELAY_ACTIONS -> true
        else -> false
    }

    fun delaysDamage(): Boolean = when (this) {
        DELAY_ACTIONS -> true
        else -> false
    }
}