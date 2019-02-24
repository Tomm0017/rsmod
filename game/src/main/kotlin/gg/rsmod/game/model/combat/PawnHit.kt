package gg.rsmod.game.model.combat

import gg.rsmod.game.model.Hit

/**
 * Represents a [Hit] dealt by a [gg.rsmod.game.model.entity.Pawn].
 *
 * @param landed
 * If the hit past the accuracy formula check (hit should land a random number
 * based on max hit)
 *
 * @author Tom <rspsmods@gmail.com>
 */
data class PawnHit(val hit: Hit, val landed: Boolean)