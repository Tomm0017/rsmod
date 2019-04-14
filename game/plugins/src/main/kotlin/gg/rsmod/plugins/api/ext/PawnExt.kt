package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.Hit
import gg.rsmod.game.model.attr.*
import gg.rsmod.game.model.entity.GameObject
import gg.rsmod.game.model.entity.Npc
import gg.rsmod.game.model.entity.Pawn
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.timer.FROZEN_TIMER
import gg.rsmod.game.model.timer.STUN_TIMER
import gg.rsmod.plugins.api.BonusSlot
import gg.rsmod.plugins.api.HitType
import gg.rsmod.plugins.api.HitbarType
import gg.rsmod.plugins.api.PrayerIcon

fun Pawn.getCommandArgs(): Array<String> = attr[COMMAND_ARGS_ATTR]!!

fun Pawn.getInteractingSlot(): Int = attr[INTERACTING_SLOT_ATTR]!!

fun Pawn.getInteractingItem(): Item = attr[INTERACTING_ITEM]!!.get()!!

fun Pawn.getInteractingItemId(): Int = attr[INTERACTING_ITEM_ID]!!

fun Pawn.getInteractingItemSlot(): Int = attr[INTERACTING_ITEM_SLOT]!!

fun Pawn.getInteractingOption(): Int = attr[INTERACTING_OPT_ATTR]!!

fun Pawn.getInteractingGameObj(): GameObject = attr[INTERACTING_OBJ_ATTR]!!.get()!!

fun Pawn.getInteractingNpc(): Npc = attr[INTERACTING_NPC_ATTR]!!.get()!!

fun Pawn.getInteractingPlayer() : Player = attr[INTERACTING_PLAYER_ATTR]!!.get()!!

fun Pawn.hasPrayerIcon(icon: PrayerIcon): Boolean = prayerIcon == icon.id

fun Pawn.getBonus(slot: BonusSlot): Int = equipmentBonuses[slot.id]

fun Pawn.hit(damage: Int, type: HitType = if (damage == 0) HitType.BLOCK else HitType.HIT, delay: Int = 0): Hit {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage, type = type.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.pixelsWide)
            .build()

    addHit(hit)
    return hit
}

fun Pawn.doubleHit(damage1: Int, damage2: Int, delay: Int = 0, type1: HitType = if (damage1 == 0) HitType.BLOCK else HitType.HIT,
                   type2: HitType = if (damage2 == 0) HitType.BLOCK else HitType.HIT): Hit {
    val hit = Hit.Builder()
            .setDamageDelay(delay)
            .addHit(damage = damage1, type = type1.id)
            .addHit(damage = damage2, type = type2.id)
            .setHitbarMaxPercentage(HitbarType.NORMAL.pixelsWide)
            .build()

    addHit(hit)
    return hit
}

fun Pawn.showHitbar(percentage: Int, type: HitbarType) {
    addHit(Hit.Builder().onlyShowHitbar().setHitbarType(type.id).setHitbarPercentage(percentage).setHitbarMaxPercentage(type.pixelsWide).build())
}

fun Pawn.freeze(cycles: Int, onFreeze: () -> Unit) {
    if (timers.has(FROZEN_TIMER)) {
        return
    }
    stopMovement()
    timers[FROZEN_TIMER] = cycles
    onFreeze()
}

fun Pawn.freeze(cycles: Int) {
    freeze(cycles) {
        if (this is Player) {
            this.message("You have been frozen.")
        }
    }
}

fun Pawn.stun(cycles: Int, onStun: () -> Unit): Boolean {
    if (timers.has(STUN_TIMER)) {
        return false
    }
    stopMovement()
    timers[STUN_TIMER] = cycles
    onStun()
    return true
}

fun Pawn.stun(cycles: Int) {
    stun(cycles) {
        if (this is Player) {
            graphic(245, 124)
            resetInteractions()
            interruptQueues()
            message("You have been stunned!")
        }
    }
}