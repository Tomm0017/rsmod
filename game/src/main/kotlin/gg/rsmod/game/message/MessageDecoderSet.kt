package gg.rsmod.game.message

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.codec.decoder.ClickMapDecoder
import gg.rsmod.game.message.codec.decoder.CommandDecoder
import gg.rsmod.game.message.codec.decoder.DisplayModeDecoder
import gg.rsmod.game.message.codec.decoder.ObjectActionOneDecoder
import gg.rsmod.game.message.impl.ChangeDisplayModeMessage
import gg.rsmod.game.message.impl.ClickMapMovementMessage
import gg.rsmod.game.message.impl.CommandMessage
import gg.rsmod.game.message.impl.ObjectActionOneMessage

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
    private val decoders = arrayOfNulls<MessageDecoder<*>>(255)

    /**
     * Links [Message]s to their respective [MessageDecoder]s.
     */
    fun init(structures: MessageStructureSet) {
        put(CommandMessage::class.java, CommandDecoder(), structures)
        put(ChangeDisplayModeMessage::class.java, DisplayModeDecoder(), structures)
        put(ClickMapMovementMessage::class.java, ClickMapDecoder(), structures)
        put(ObjectActionOneMessage::class.java, ObjectActionOneDecoder(), structures)
    }

    private fun put(messageType: Class<out Message>, decoderType: MessageDecoder<out Message>, structures: MessageStructureSet) {
        val structure = structures.get(messageType)!!
        decoders[structure.opcode] = decoderType
    }

    @Suppress("UNCHECKED_CAST")
    fun get(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }
}