package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * A decoupled file that holds extensions and helper functions, related to plugins,
 * that can be used throughout plugins.
 *
 * @author Tom <rspsmods@gmail.com>
 */

/**
 * Gets the [ctx] as a [Pawn]. If [ctx] is not a [Pawn], a cast exception
 * will be thrown.
 */
inline val Plugin.pawn: Pawn get() = ctx as Pawn

/**
 * Gets the [ctx] as a [Player]. If [ctx] is not a [Player], a cast exception
 * will be thrown.
 */
inline val Plugin.player: Player get() = ctx as Player

/**
 * Gets the [ctx] as an [Npc]. If [ctx] is not an [Npc], a cast exception
 * will be thrown.
 */
inline val Plugin.npc: Npc get() = ctx as Npc