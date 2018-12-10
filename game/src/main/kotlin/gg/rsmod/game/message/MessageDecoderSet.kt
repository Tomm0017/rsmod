package gg.rsmod.game.message

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.codec.decoder.*
import gg.rsmod.game.message.impl.*

/**
 * Stores all the [MessageDecoder]s that are used on the
 * [gg.rsmod.game.service.GameService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MessageDecoderSet {

    /**
     * The [MessageDecoder]s stored in respect to their opcode.
     */
    private val decoders = arrayOfNulls<MessageDecoder<*>>(256)

    /**
     * Links [Message]s to their respective [MessageDecoder]s.
     */
    fun init(structures: MessageStructureSet) {
        put(CommandMessage::class.java, CommandDecoder(), structures)
        put(ChangeDisplayModeMessage::class.java, DisplayModeDecoder(), structures)
        put(ClickMapMovementMessage::class.java, ClickMapDecoder(), structures)
        put(ObjectActionOneMessage::class.java, ObjectActionOneDecoder(), structures)
        put(ClickButtonMessage::class.java, ClickButtonDecoder(), structures)
        put(ContinueDialogMessage::class.java, ContinueDialogDecoder(), structures)
        put(ExamineObjectMessage::class.java, ExamineObjectDecoder(), structures)
    }

    private fun put(messageType: Class<out Message>, decoderType: MessageDecoder<out Message>, structures: MessageStructureSet) {
        val structure = structures.get(messageType)!!
        structure.opcodes.forEach { opcode ->
            decoders[opcode] = decoderType
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun get(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }
}