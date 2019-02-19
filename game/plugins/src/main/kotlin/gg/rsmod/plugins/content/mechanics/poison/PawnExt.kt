package gg.rsmod.plugins.content.mechanics.poison

import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */

fun Pawn.poison(initialDamage: Int, onPoison: () -> Unit) {
    if (!Poison.isImmune(this) && Poison.poison(this, initialDamage)) {
        if (this is Player) {
            Poison.setHpOrb(this, Poison.OrbState.POISON)
        }
        onPoison()
    }
}