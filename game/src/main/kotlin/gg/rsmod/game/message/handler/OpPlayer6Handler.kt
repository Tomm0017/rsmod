package gg.rsmod.game.message.handler

import gg.rsmod.game.action.PawnPathAction
import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.*
import gg.rsmod.game.model.attr.INTERACTING_OPT_ATTR
import gg.rsmod.game.model.attr.INTERACTING_PLAYER_ATTR
import gg.rsmod.game.model.entity.Client
import gg.rsmod.game.model.entity.PlayerOption
import java.lang.ref.WeakReference

/**
 * @author Triston Plummer ("Dread")
 */
class OpPlayer6Handler : MessageHandler<OpPlayer6Message> {

    override fun handle(client: Client, message: OpPlayer6Message) {
        val index = message.index
        val option = PlayerOption.byIndex(5) ?: return

        if (!client.lock.canPlayerInteract()) {
            return
        }

        val other = client.world.players[index] ?: return

        if (!other.hasOption(option)) {
            return
        }

        log(client, "Player option: name=%s, opt=%d", other.username, option.index)

        client.attr[INTERACTING_PLAYER_ATTR] = WeakReference(other)
        client.attr[INTERACTING_OPT_ATTR] = option.index
        client.executePlugin(PawnPathAction.walkPlugin)
    }

}