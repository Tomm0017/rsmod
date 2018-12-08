package gg.rsmod.game.model

/**
 * A decoupled file that holds [AttributeKey]s that require read-access from our
 * game module. Any attributes that can be stored on the plugin classes themselves,
 * should do so. When storing them in a class, remember the [AttributeKey] must be
 * a singleton, meaning it should only have a single state.
 *
 * @author Tom <rspsmods@gmail.com>
 */

/**
 * The index that was assigned to a [Player] when they were first registered to the
 * [World]. This is needed to remove local players from the synchronization task
 * as once that logic is reached, the local player would have an index of [-1].
 */
val INDEX_ATTR = AttributeKey<Int>()

/**
 * The command that the player has submitted to the server using the '::' prefix.
 */
val COMMAND_ATTR = AttributeKey<String>()

/**
 * The arguments to the last command that was submitted by the player. This does
 * not include the command itself, if you want the command itself, use the
 * [COMMAND_ATTR] attribute.
 */
val COMMAND_ARGS_ATTR = AttributeKey<Array<String>>()