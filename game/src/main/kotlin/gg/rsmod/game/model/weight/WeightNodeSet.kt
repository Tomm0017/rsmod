package gg.rsmod.game.model.weight

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.security.SecureRandom
import java.util.*

/**
 * Represents a set of [WeightNode]s.
 *
 * @author Tom <rspsmods@gmail.com>
 */
open class WeightNodeSet<T> {

    /**
     * All the [WeightNode]s that this set represents.
     */
    private val nodes = ObjectArrayList<WeightNode<T>>()

    private val random = SecureRandom()

    open fun add(node: WeightNode<T>): WeightNodeSet<T> {
        nodes.add(node)
        return this
    }

    fun getRandom(random: Random = this.random): T = getRandomNode(random).convert(random)

    fun getRandomNode(random: Random = this.random): WeightNode<T> {
        val totalWeight = nodes.sumOf { it.weight }
        var randomWeight = random.nextInt(totalWeight + 1)

        for (node in nodes) {
            randomWeight -= node.weight
            if (randomWeight <= 0) {
                return node
            }
        }

        throw RuntimeException() // This will never happen.
    }
}
