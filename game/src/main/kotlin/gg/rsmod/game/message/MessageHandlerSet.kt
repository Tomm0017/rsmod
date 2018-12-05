package gg.rsmod.game.message

import gg.rsmod.game.message.handler.ClickMapHandler
import gg.rsmod.game.message.handler.CommandHandler
import gg.rsmod.game.message.handler.DisplayModeHandler
import gg.rsmod.game.message.handler.ObjectActionOneHandler
import gg.rsmod.game.message.impl.ChangeDisplayModeMessage
import gg.rsmod.game.message.impl.ClickMapMovementMessage
import gg.rsmod.game.message.impl.CommandMessage
import gg.rsmod.game.message.impl.ObjectActionOneMessage

/**
 * Stores all the [MessageHandler]s that are used on the
 * [gg.rsmod.game.service.GameService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MessageHandlerSet {

    /**
     * The [MessageHandler]s stored in respect to their opcode.
     */
    private val handlers = arrayOfNulls<MessageHandler<out Message>>(255)

    /**
     * Links [Message]s to their respective [MessageHandler]s.
     */
    fun init(structures: MessageStructureSet) {
        put(CommandMessage::class.java, CommandHandler(), structures)
        put(ChangeDisplayModeMessage::class.java, DisplayModeHandler(), structures)
        put(ClickMapMovementMessage::class.java, ClickMapHandler(), structures)
        put(ObjectActionOneMessage::class.java, ObjectActionOneHandler(), structures)
    }

    private fun put(messageType: Class<out Message>, handlerType: MessageHandler<out Message>, structures: MessageStructureSet) {
        val structure = structures.get(messageType)!!
        this.handlers[structure.opcode] = handlerType
    }

    @Suppress("UNCHECKED_CAST")
    fun getHandler(opcode: Int): MessageHandler<Message>? {
        return handlers[opcode] as MessageHandler<Message>?
    }
}