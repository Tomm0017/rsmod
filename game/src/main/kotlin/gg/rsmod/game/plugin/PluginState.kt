package gg.rsmod.game.plugin

/**
 * Represents a valid state in a plugin that's being executed by an
 * entity context.
 *
 * @author Tom <rspsmods@gmail.com>
 */
enum class PluginState {

    /**
     * The neutral state on a plugin. A plugin is in this state
     * by default.
     */
    NORMAL,

    /**
     * The plugin must wait for a specified amount of game cycles
     * before continuing.
     */
    TIME_WAIT,

    /**
     * The plugin must wait for the context (usually a player in this case)
     * to continue a dialog before continuing.
     */
    DIALOG_WAIT,

    /**
     * The plugin was interrupted by another action, such as walking away.
     */
    INTERRUPTED
}