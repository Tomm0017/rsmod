package gg.rsmod.plugins.api.ext

import gg.rsmod.game.model.attr.*
import gg.rsmod.game.model.entity.Pawn

val Pawn.facingPawn by FACING_PAWN_ATTR
val Pawn.npcFacingMe by NPC_FACING_US_ATTR
val Pawn.combatTarget by COMBAT_TARGET_FOCUS_ATTR
val Pawn.killer by KILLER_ATTR
val Pawn.lastPawnHit by LAST_HIT_ATTR
val Pawn.lastHitBy by LAST_HIT_BY_ATTR
val Pawn.poisonTicks by POISON_TICKS_LEFT_ATTR.defaultValue(0)
val Pawn.isDragonfireImmune by DRAGONFIRE_IMMUNITY_ATTR.defaultValue(false)
