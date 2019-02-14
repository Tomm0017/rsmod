package gg.rsmod.game.model.weight

import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.util.*

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WeightNodeSet<T> {

    private val nodes = ObjectArrayList<WeightNode<T>>()

    fun add(node: WeightNode<T>): WeightNodeSet<T> {
        nodes.add(node)
        return this
    }

    fun getRandom(random: Random): T = getRandomNode(random).value

    fun getRandomNode(random: Random): WeightNode<T> {
        val totalWeight = nodes.sumBy { it.weight }
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
