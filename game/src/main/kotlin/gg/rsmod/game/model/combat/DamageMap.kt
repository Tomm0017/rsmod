package gg.rsmod.game.model.combat

import gg.rsmod.game.model.EntityType
import gg.rsmod.game.model.entity.Pawn
import java.util.*

/**
 * Represents a map of hits from different [Pawn]s and their information.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class DamageMap {

    private val map = WeakHashMap<Pawn, DamageStack>(0)

    operator fun get(pawn: Pawn): DamageStack? = map[pawn]

    fun add(pawn: Pawn, damage: Int) {
        val total = (map[pawn]?.totalDamage ?: 0) + damage
        map[pawn] = DamageStack(total, System.currentTimeMillis())
    }

    /**
     * Get all [DamageStack]s dealt by [Pawn]s whom meets the criteria
     * [Pawn.getType] == [type].
     */
    fun getAll(type: EntityType, timeFrameMs: Long? = null): Collection<DamageStack> = map.filter { it.key.getType() == type && (timeFrameMs == null || System.currentTimeMillis() - it.value.lastHit < timeFrameMs) }.values

    /**
     * Get the total damage from a [pawn].
     *
     * @return
     * 0 if [pawn] has not dealt any damage.
     */
    fun getDamageFrom(pawn: Pawn): Int = map[pawn]?.totalDamage ?: 0

    /**
     * Gets the [Pawn] that has dealt the most damage in this map.
     */
    fun getMostDamage(): Pawn? = map.maxBy { it.value.totalDamage }?.key

    /**
     * Gets the most damage dealt by a [Pawn] in our map whom meets the criteria
     * [Pawn.getType] == [type].
     */
    fun getMostDamage(type: EntityType, timeFrameMs: Long? = null): Pawn? = map.filter { it.key.getType() == type && (timeFrameMs == null || System.currentTimeMillis() - it.value.lastHit < timeFrameMs) }.maxBy { it.value.totalDamage }?.key

    data class DamageStack(val totalDamage: Int, val lastHit: Long)
}