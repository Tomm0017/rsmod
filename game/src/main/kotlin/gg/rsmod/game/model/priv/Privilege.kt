package gg.rsmod.game.model.priv

/**
 * Represents privilege levels.
 *
 * @param id
 * The unique id of the privilege
 *
 * @param icon
 * The icon id that is used on the client to show the proper crown for the
 * privilege.
 *
 * @param name
 * The name of the privilege.
 *
 * @param powers
 * The name of the "powers" that this privilege has access to.
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class Privilege(val id: Int, val icon: Int, val name: String, val powers: Set<String>) {

    companion object {

        /**
         * The global identifier used for developers. This identifier should be
         * used globally to identify a player with developer privileges.
         */
        const val DEV_POWER = "dev"

        /**
         * The global identifier used for administrators. This identifier should
         * be used globally to identify a player with administrator privileges.
         */
        const val ADMIN_POWER = "admin"

        /**
         * The global identifier used for owners. This identifier should be
         * used globally to identify a player with owner privileges.
         */
        const val OWNER_POWER = "owner"

        /**
         * The default privilege level.
         */
        val DEFAULT = Privilege(id = 0, icon = 0, name = "Player", powers = emptySet())
    }
}