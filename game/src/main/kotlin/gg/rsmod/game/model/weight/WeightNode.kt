package gg.rsmod.game.model.weight

/**
 * A weighted node that can be used in a weighted distribution system.
 *
 * @param T
 * The type that this node will represent.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class WeightNode<T>(val weight: Int, val value: T)