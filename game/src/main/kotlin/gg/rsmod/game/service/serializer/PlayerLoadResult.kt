package gg.rsmod.game.service.serializer

/**
 * Possible results when trying to decode player data.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class PlayerLoadResult {
    /**
     * The account has never logged into the game before.
     */
    NEW_ACCOUNT,

    /**
     * The account has previously been made.
     */
    LOAD_ACCOUNT,

    /**
     * The credentials provided at login are incorrect.
     */
    INVALID_CREDENTIALS,

    /**
     * The log-in xteas did not match the previous session.
     */
    INVALID_RECONNECTION,

    /**
     * There was an error decoding the data.
     */
    MALFORMED
}