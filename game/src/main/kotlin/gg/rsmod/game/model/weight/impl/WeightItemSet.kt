package gg.rsmod.game.model.weight.impl

import gg.rsmod.game.model.item.Item
import gg.rsmod.game.model.weight.WeightNode
import gg.rsmod.game.model.weight.WeightNodeSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class WeightItemSet : WeightNodeSet<Item>() {

    override fun add(node: WeightNode<Item>): WeightItemSet {
        super.add(node)
        return this
    }
}