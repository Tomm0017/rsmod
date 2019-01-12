package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.SearchedItemMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SearchedItemHandler : MessageHandler<SearchedItemMessage> {

    override fun handle(client: Client, message: SearchedItemMessage) {
        log(client, "Searched item: item=%d", message.item)
        client.world.pluginExecutor.submitReturnType(client, message.item)
    }
}