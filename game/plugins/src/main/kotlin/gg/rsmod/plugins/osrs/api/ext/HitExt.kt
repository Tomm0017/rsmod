package gg.rsmod.plugins.osrs.api.ext

import gg.rsmod.game.model.combat.PawnHit

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun PawnHit.landed(): Boolean = landed

fun PawnHit.blocked(): Boolean = !landed

fun PawnHit.getClientHitDelay(): Int = hit.damageDelay * 30