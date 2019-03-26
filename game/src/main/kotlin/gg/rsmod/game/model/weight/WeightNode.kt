package gg.rsmod.game.model.weight

import java.util.*

/**
 * A weighted node that can be used in a weighted distribution system.
 *
 * @param T
 * The type that this node will represent.
 *
 * @author Tom <rspsmods@gmail.com>
 */
abstract class WeightNode<T>(val weight: Int) {

    /**
     * Convert the [WeightNode] to [T].
     */
    abstract fun convert(random: Random): T
}