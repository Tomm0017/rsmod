package gg.rsmod.game.model.container.listener

import gg.rsmod.game.model.entity.Player

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface ItemContainerListener {

    fun clean(player: Player)
}