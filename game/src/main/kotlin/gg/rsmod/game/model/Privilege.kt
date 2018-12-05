package gg.rsmod.game.model

/**
 * Represents privilege levels.
 *
 * @param id
 * The unique id of the privilege
 *
 * @param power
 * The power level of the privilege. Higher power level means more authority on
 * the server.
 *
 * @param icon
 * The icon id that is used on the client to show the proper crown for the
 * privilege.
 *
 * @param name
 * The name of the privilege.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Privilege(val id: Int, val power: Int, val icon: Int, val name: String) {

    /**
     * Checks if this privilege is equal to or above in power to the [other]
     * privilege.
     */
    fun isEligible(other: Privilege): Boolean = power >= other.power

    companion object {
        /**
         * The default privilege level.
         */
        val DEFAULT = Privilege(0, 0, -1, "Player")
    }
}