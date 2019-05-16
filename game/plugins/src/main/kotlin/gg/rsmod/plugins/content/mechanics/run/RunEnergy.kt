package gg.rsmod.plugins.content.mechanics.run

import gg.rsmod.game.model.bits.INFINITE_VARS_STORAGE
import gg.rsmod.game.model.bits.InfiniteVarsType
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.model.timer.TimerKey
import gg.rsmod.plugins.api.EquipmentType
import gg.rsmod.plugins.api.Skills
import gg.rsmod.plugins.api.ext.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RunEnergy {

    val RUN_DRAIN = TimerKey()

    /**
     * Reduces run energy depletion by 70%
     */
    val STAMINA_BOOST = TimerKey("stamina_boost", tickOffline = false)

    private const val RUN_ENABLED_VARP = 173

    fun toggle(p: Player) {
        if (p.runEnergy >= 1.0) {
            p.toggleVarp(RUN_ENABLED_VARP)
        } else {
            p.setVarp(RUN_ENABLED_VARP, 0)
            p.message("You don't have enough run energy left.")
        }
    }

    fun drain(p: Player) {
        if (p.isRunning() && p.hasMoveDestination()) {
            if (!p.hasStorageBit(INFINITE_VARS_STORAGE, InfiniteVarsType.RUN)) {
                val weight = Math.max(0.0, p.weight)
                var decrement = (Math.min(weight, 64.0) / 100.0) + 0.64
                if (p.timers.has(STAMINA_BOOST)) {
                    decrement *= 0.3
                }
                p.runEnergy = Math.max(0.0, (p.runEnergy - decrement))
                if (p.runEnergy <= 0) {
                    p.varps.setState(RUN_ENABLED_VARP, 0)
                }
                p.sendRunEnergy(p.runEnergy.toInt())
            }
        } else if (p.runEnergy < 100.0 && p.lock.canRestoreRunEnergy()) {
            var recovery = (8.0 + (p.getSkills().getCurrentLevel(Skills.AGILITY) / 6.0)) / 100.0
            if (isWearingFullGrace(p)) {
                recovery *= 1.3
            }
            p.runEnergy = Math.min(100.0, (p.runEnergy + recovery))
            p.sendRunEnergy(p.runEnergy.toInt())
        }
    }

    private fun isWearingFullGrace(p: Player): Boolean =
            p.equipment[EquipmentType.HEAD.id]?.id ?: -1 in GRACEFUL_HOODS &&
                    p.equipment[EquipmentType.CAPE.id]?.id ?: -1 in GRACEFUL_CAPE &&
                    p.equipment[EquipmentType.CHEST.id]?.id ?: -1 in GRACEFUL_TOP &&
                    p.equipment[EquipmentType.LEGS.id]?.id ?: -1 in GRACEFUL_LEGS &&
                    p.equipment[EquipmentType.GLOVES.id]?.id ?: -1 in GRACEFUL_GLOVES &&
                    p.equipment[EquipmentType.BOOTS.id]?.id ?: -1 in GRACEFUL_BOOTS

    private val GRACEFUL_HOODS = intArrayOf(11850, 13579, 13591, 13603, 13615, 13627, 13667, 21061)

    private val GRACEFUL_CAPE = intArrayOf(11852, 13581, 13593, 13605, 13617, 13629, 13669, 21064)

    private val GRACEFUL_TOP = intArrayOf(11854, 13583, 13595, 13607, 13619, 13631, 13671, 21067)

    private val GRACEFUL_LEGS = intArrayOf(11856, 13585, 13597, 13609, 13621, 13633, 13673, 21070)

    private val GRACEFUL_GLOVES = intArrayOf(11858, 13587, 13599, 13611, 13623, 13635, 13675, 21073)

    private val GRACEFUL_BOOTS = intArrayOf(11860, 13589, 13601, 13613, 13625, 13637, 13677, 21076)
}