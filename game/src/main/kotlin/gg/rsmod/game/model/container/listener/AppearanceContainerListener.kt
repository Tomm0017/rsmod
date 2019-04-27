package gg.rsmod.game.model.container.listener

import gg.rsmod.game.model.entity.Player
import gg.rsmod.game.sync.block.UpdateBlockType

/**
 * @author Tom <rspsmods@gmail.com>
 */
object AppearanceContainerListener : ItemContainerListener {

    override fun clean(player: Player) {
        player.addBlock(UpdateBlockType.APPEARANCE)
    }
}