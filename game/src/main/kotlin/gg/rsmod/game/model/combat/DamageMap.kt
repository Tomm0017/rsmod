package gg.rsmod.game.model.combat

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.entity.Pawn

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DamageMap {

    private val map = hashMapOf<Pawn, DamageStack>()

    operator fun set(pawn: Pawn, damage: Int) {
        val total = (map[pawn]?.totalDamage ?: 0) + damage
        map[pawn] = DamageStack(total, System.currentTimeMillis())
    }

    operator fun get(pawn: Pawn): DamageStack? = map[pawn]

    fun getAll(type: EntityType, timeFrameMs: Long? = null): Collection<DamageStack> = map.filter { it.key.getType() == type && (timeFrameMs == null || System.currentTimeMillis() - it.value.lastHit < timeFrameMs) }.values

    fun getDamageFrom(pawn: Pawn): Int = map[pawn]?.totalDamage ?: 0

    fun getMostDamage(): Pawn? = map.maxBy { it.value.totalDamage }?.key

    fun getMostDamage(type: EntityType, timeFrameMs: Long? = null): Pawn? = map.filter { it.key.getType() == type && (timeFrameMs == null || System.currentTimeMillis() - it.value.lastHit < timeFrameMs) }.maxBy { it.value.totalDamage }?.key

    data class DamageStack(val totalDamage: Int, val lastHit: Long)
}