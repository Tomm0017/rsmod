package gg.rsmod.game.model

import gg.rsmod.game.model.Hit.Hitbar
import gg.rsmod.game.model.Hit.Hitmark
import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * Represents a hit that can be dealt. A hit can deal multiple damage splats
 * ([Hitmark]s).
 *
 * @param hitmarks
 * A list of all the [Hitmark]s this [Hit] will deal.
 *
 * @param hitbar
 * The [Hitbar] this hit will cause to appear, if any. If this hit should not show
 * a hitbar, this value should be null.
 *
 * @param clientDelay
 * The delay for the client to show this hit.
 *
 * @param damageDelay
 * The delay for the server to handle the [hitmarks]s and deal damage, in
 * game cycles.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Hit private constructor(val hitmarks: List<Hitmark>, val hitbar: Hitbar?, val clientDelay: Int, var damageDelay: Int) {

    /**
     * A list of actions that will be invoked upon this hit dealing damage
     * to whichever entity it may.
     *
     * There is no guarantee that this action will occur under certain
     * circumstances, such as if the entity dies before this hit deals
     * damage or if the hit is cancelled.
     */
    internal val actions = ObjectArrayList<() -> Unit>()

    /**
     * Predicate that determines if this hit should be cancelled, such as if the
     * origin of the hit has died.
     */
    internal var cancelCondition: () -> Boolean = { false }

    /**
     * @see actions
     */
    fun addAction(action: () -> Unit): Hit {
        actions.add(action)
        return this
    }

    /**
     * @see actions
     */
    fun addActions(actions: Collection<() -> Unit>): Hit {
        this.actions.addAll(actions)
        return this
    }

    /**
     * @see cancelCondition
     */
    fun setCancelIf(condition: () -> Boolean): Hit {
        cancelCondition = condition
        return this
    }

    data class Hitmark(val type: Int, var damage: Int)

    data class Hitbar(val type: Int, val percentage: Int, val maxPercentage: Int, val depleteSpeed: Int, val delay: Int)

    class Builder {

        private val hitmarks = mutableListOf<Hitmark>()

        private var onlyShowHitbar = false

        private var hideHitbar = false

        private var hitbarType = -1

        private var hitbarDepleteSpeed = -1

        private var hitbarPercentage = -1

        private var hitbarMaxPercentage = -1

        private var hitbarDelay = -1

        private var clientDelay = -1

        private var damageDelay = -1

        fun build(): Hit {
            check(onlyShowHitbar || hitmarks.isNotEmpty()) { "You can't build a Hit with no hitmarkers unless the appropriate flag is set (Builder#addHit or Builder#onlyShowHitbar)" }
            check(!onlyShowHitbar || !hideHitbar) { "You can't have both of these flags set (Builder#hideHitbar | Builder#onlyShowHitbar)" }
            check(hideHitbar || hitbarDepleteSpeed == -1 || hitbarDepleteSpeed == 0 || hitbarMaxPercentage != -1) { "Hitbar deplete speed cannot be > 0 unless max percentage is set (Builder#setHitbarMaxPercentage)" }
            check(damageDelay == -1 || damageDelay >= 0) { "Damage delay must be positive" }

            if (clientDelay == -1) {
                clientDelay = 0
            }

            if (hitbarType == -1) {
                hitbarType = 0
            }

            if (hitbarDepleteSpeed == -1) {
                hitbarDepleteSpeed = 0
            }

            if (hitbarDelay == -1) {
                hitbarDelay = 0
            }

            if (damageDelay == -1) {
                damageDelay = 0
            }

            val hitbar = if (!hideHitbar) Hitbar(hitbarType, hitbarPercentage, hitbarMaxPercentage, hitbarDepleteSpeed, hitbarDelay) else null
            return Hit(hitmarks, hitbar, clientDelay, damageDelay)
        }

        /**
         * @see Hit.hitmarks
         */
        fun addHit(damage: Int, type: Int): Builder {
            hitmarks.add(Hitmark(type, damage))
            return this
        }

        /**
         * @see Hit.clientDelay
         */
        fun setClientDelay(delay: Int): Builder {
            this.clientDelay = delay
            return this
        }

        /**
         * @see Hit.damageDelay
         */
        fun setDamageDelay(delay: Int): Builder {
            this.damageDelay = delay
            return this
        }

        /**
         * Only show the hitbar, but no damage on our [Hit].
         */
        fun onlyShowHitbar(): Builder {
            this.onlyShowHitbar = true
            return this
        }

        /**
         * Hide the hitbar, but still show the damage for our [Hit].
         */
        fun hideHitbar(): Builder {
            this.hideHitbar = true
            return this
        }

        /**
         * @see Hitbar.type
         */
        fun setHitbarType(type: Int): Builder {
            this.hitbarType = type
            return this
        }

        /**
         * @see Hitbar.depleteSpeed
         */
        fun setHitbarDepleteSpeed(depleteSpeed: Int): Builder {
            this.hitbarDepleteSpeed = depleteSpeed
            return this
        }

        /**
         * @see Hitbar.percentage
         */
        fun setHitbarPercentage(percentage: Int): Builder {
            this.hitbarPercentage = percentage
            return this
        }

        /**
         * @see Hitbar.maxPercentage
         */
        fun setHitbarMaxPercentage(percentage: Int): Builder {
            this.hitbarMaxPercentage = percentage
            return this
        }

        /**
         * @see Hitbar.delay
         */
        fun setHitbarDelay(delay: Int): Builder {
            this.hitbarDelay = delay
            return this
        }
    }
}