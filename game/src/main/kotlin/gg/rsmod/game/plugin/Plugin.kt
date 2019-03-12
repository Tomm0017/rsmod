package gg.rsmod.game.plugin

import gg.rsmod.game.model.entity.Player

/**
 * Represents a plugin that can be executed at any time by a context.
 *
 * @param ctx
 * Can be anything from [Player] to [gg.rsmod.game.model.entity.Pawn].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Plugin(var ctx: Any?)