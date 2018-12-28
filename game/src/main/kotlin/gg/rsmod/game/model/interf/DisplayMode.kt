package gg.rsmod.game.model.interf

/**
 * Represents a display-mode that the client can use.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class DisplayMode {
    /**
     * The game client is in fixed display-mode.
     */
    FIXED,

    /**
     * The game client is in resizable display-mode.
     */
    RESIZABLE_NORMAL,

    /**
     * The game client is in resizable display-mode, with the list view for its
     * gameframe.
     */
    RESIZABLE_LIST,

    /**
     * The game client is in mobile display-mode.
     */
    MOBILE,

    /**
     * The game client is in resizable display-mode. This is not supported for
     * lower revisions.
     */
    FULLSCREEN,
}