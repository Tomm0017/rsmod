package gg.rsmod.game.message.handler

import gg.rsmod.game.message.MessageHandler
import gg.rsmod.game.message.impl.CloseMainInterfaceMessage
import gg.rsmod.game.model.entity.Client

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CloseMainInterfaceHandler : MessageHandler<CloseMainInterfaceMessage> {

    override fun handle(client: Client, message: CloseMainInterfaceMessage) {
        // TODO(Tom): decide how we want to handle the following:
        //
        // NOTE(Tom): decide whether we want to close the main interface behind-
        // the-scenes in Interfaces from the visible map. And also, how we decide
        // to keep track of it. We can go the plugin route, which means the user
        // is responsible for the plugin which will close the main screen interface
        // as this can vary from revision to revision or we can add a variable in
        // Interfaces that holds the "main screen" interface id, which we'll close
        // when this message is received. In the case of the latter, we will need
        // to allow the user to set the "main screen" interface id somehow and they
        // must set it when appropriate. Both ways have their cons.
        //
        // As this message is handled before the button click message (when clicking
        // on the X for main interfaces), if we close it on the same cycle, it will
        // lead to the button warning us that the player is trying to click a button
        // on an interface that's not visible (anymore). This is the strongest reason
        // against even handling the closing of the main screen interface and just
        // letting it stay as "visible" behind-the-scenes (though this could also lead
        // to trying to bank items without having the interface open, which is a pretty
        // big flaw which will lead to a bunch of cheap fixes). What we can do to fix the
        // button warning is to have a queue of interface ids to close next cycle.
    }
}