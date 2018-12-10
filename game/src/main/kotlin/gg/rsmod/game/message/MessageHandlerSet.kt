package gg.rsmod.game.message

import gg.rsmod.game.message.handler.*
import gg.rsmod.game.message.impl.*

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
    private val handlers = arrayOfNulls<MessageHandler<out Message>>(256)

    /**
     * Links [Message]s to their respective [MessageHandler]s.
     */
    fun init(structures: MessageStructureSet) {
        put(CommandMessage::class.java, CommandHandler(), structures)
        put(ChangeDisplayModeMessage::class.java, DisplayModeHandler(), structures)
        put(ClickMapMovementMessage::class.java, ClickMapHandler(), structures)
        put(ObjectActionOneMessage::class.java, ObjectActionOneHandler(), structures)
        put(ClickButtonMessage::class.java, ClickButtonHandler(), structures)
        put(ContinueDialogMessage::class.java, ContinueDialogHandler(), structures)
        put(ExamineObjectMessage::class.java, ExamineObjectHandler(), structures)
    }

    private fun put(messageType: Class<out Message>, handlerType: MessageHandler<out Message>, structures: MessageStructureSet) {
        val structure = structures.get(messageType)!!
        structure.opcodes.forEach { opcode ->
            this.handlers[opcode] = handlerType
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun getHandler(opcode: Int): MessageHandler<Message>? {
        return handlers[opcode] as MessageHandler<Message>?
    }
}