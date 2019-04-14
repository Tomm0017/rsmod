package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.Plugin

/**
 * Gets the [Plugin.ctx] as a [Pawn].
 *
 * If [Plugin.ctx] is not a [Pawn], a cast exception will be thrown.
 */
inline val Plugin.pawn: Pawn get() = ctx as Pawn

/**
 * Gets the [Plugin.ctx] as a [Player].
 *
 * If [Plugin.ctx] is not a [Player], a cast exception will be thrown.
 */
inline val Plugin.player: Player get() = ctx as Player

/**
 * Gets the [Plugin.ctx] as an [Npc].
 *
 * If [Plugin.ctx] is not an [Npc], a cast exception will be thrown.
 */
inline val Plugin.npc: Npc get() = ctx as Npc