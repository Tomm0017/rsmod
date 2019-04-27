package gg.rsmod.game.model.container.listener

import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
object BonusContainerListener : ItemContainerListener {

    override fun clean(player: Player) {
        player.calculateBonuses()
    }
}