package gg.rsmod.plugins.osrs.content

import gg.rsmod.game.message.impl.SetRunEnergyMessage
import gg.rsmod.game.model.TimerKey
import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.plugin.PluginRepository
import gg.rsmod.game.plugin.ScanPlugins
import gg.rsmod.plugins.osrs.OSRSGameframe
import gg.rsmod.plugins.osrs.model.Equipment
import gg.rsmod.plugins.osrs.model.Skills
import gg.rsmod.plugins.player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object RunEnergy {

    private val RUN_DRAIN = TimerKey()

    /**
     * Reduces run energy depletion by 70%
     */
    val STAMINA_BOOST = TimerKey("stamina_boost", tickOffline = false)

    @JvmStatic
    @ScanPlugins
    fun register(r: PluginRepository) {
        r.bindLogin {
            it.player().timers[RUN_DRAIN] = 1
        }

        r.bindTimer(RUN_DRAIN) {
            val p = it.player()

            p.timers[RUN_DRAIN] = 1

            if (p.isRunning() && p.movementQueue.hasDestination()) {
                val weight = Math.max(0.0, p.weight)
                var decrement = (Math.min(weight, 64.0) / 100.0) + 0.64
                if (p.timers.has(STAMINA_BOOST)) {
                    decrement *= 0.3
                }
                p.runEnergy = Math.max(0.0, (p.runEnergy - decrement))
                if (p.runEnergy <= 0) {
                    p.varps.setState(OSRSGameframe.RUN_ENABLED_VARP, 0)
                }
                p.write(SetRunEnergyMessage(p.runEnergy.toInt()))
            } else if (p.runEnergy < 100.0 && !p.lock.canRestoreRunEnergy()) {
                var recovery = 8.0 + (p.getSkills().getCurrentLevel(Skills.AGILITY) / 6.0)
                if (isWearingFullGrace(p)) {
                    recovery *= 1.3
                }
                p.runEnergy = Math.min(100.0, (p.runEnergy + recovery))
                p.write(SetRunEnergyMessage(p.runEnergy.toInt()))
            }
        }
    }

    private fun isWearingFullGrace(p: Player): Boolean =
            p.equipment[Equipment.HEAD.id]?.id ?: -1 in GRACEFUL_HOODS &&
                    p.equipment[Equipment.CAPE.id]?.id ?: -1 in GRACEFUL_CAPE &&
                    p.equipment[Equipment.CHEST.id]?.id ?: -1 in GRACEFUL_TOP &&
                    p.equipment[Equipment.LEGS.id]?.id ?: -1 in GRACEFUL_LEGS &&
                    p.equipment[Equipment.GLOVES.id]?.id ?: -1 in GRACEFUL_GLOVES &&
                    p.equipment[Equipment.BOOTS.id]?.id ?: -1 in GRACEFUL_BOOTS

    private val GRACEFUL_HOODS = intArrayOf(11850, 13579, 13591, 13603, 13615, 13627, 13667, 21061)

    private val GRACEFUL_CAPE = intArrayOf(11852, 13581, 13593, 13605, 13617, 13629, 13669, 21064)

    private val GRACEFUL_TOP = intArrayOf(11854, 13583, 13595, 13607, 13619, 13631, 13671, 21067)

    private val GRACEFUL_LEGS = intArrayOf(11856, 13585, 13597, 13609, 13621, 13633, 13673, 21070)

    private val GRACEFUL_GLOVES = intArrayOf(11858, 13587, 13599, 13611, 13623, 13635, 13675, 21073)

    private val GRACEFUL_BOOTS = intArrayOf(11860, 13589, 13601, 13613, 13625, 13637, 13677, 21076)
}