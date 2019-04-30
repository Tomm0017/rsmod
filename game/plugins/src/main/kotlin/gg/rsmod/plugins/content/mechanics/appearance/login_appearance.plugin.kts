package gg.rsmod.plugins.content.mechanics.appearance

import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.game.sync.block.UpdateBlockType

on_login {
    if (player.attr[NEW_ACCOUNT_ATTR] == true) {
        player.queue(TaskPriority.WEAK) {
            val appearance = selectAppearance() ?: return@queue
            player.appearance = appearance
            player.addBlock(UpdateBlockType.APPEARANCE)
        }
    }
}