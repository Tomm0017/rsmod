package gg.rsmod.game.message

import gg.rsmod.game.message.codec.MessageDecoder
import gg.rsmod.game.message.codec.decoder.*
import gg.rsmod.game.message.handler.*
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
     * The [MessageHandler]s stored in respect to their opcode.
     */
    private val handlers = arrayOfNulls<MessageHandler<out Message>>(256)

    /**
     * Links [Message]s to their respective [MessageDecoder]s and [MessageHandler].
     */
    fun init(structures: MessageStructureSet) {
        put(CommandMessage::class.java, CommandDecoder(), CommandHandler(), structures)
        put(ChangeDisplayModeMessage::class.java, DisplayModeDecoder(), DisplayModeHandler(), structures)
        put(ClickMapMessage::class.java, ClickMapDecoder(), ClickMapHandler(), structures)
        put(ClickMinimapMessage::class.java, ClickMinimapDecoder(), ClickMinimapHandler(), structures)
        put(ObjectActionOneMessage::class.java, ObjectActionOneDecoder(), ObjectActionOneHandler(), structures)
        put(ClickButtonMessage::class.java, ClickButtonOneDecoder(), ClickButtonHandler(), structures)
        put(ContinueDialogMessage::class.java, ContinueDialogDecoder(), ContinueDialogHandler(), structures)
        put(ExamineObjectMessage::class.java, ExamineObjectDecoder(), ExamineObjectHandler(), structures)
        put(ItemActionOneMessage::class.java, ItemActionOneDecoder(), ItemActionOneHandler(), structures)
        put(CloseMainInterfaceMessage::class.java, CloseMainInterfaceDecoder(), CloseMainInterfaceHandler(), structures)
        put(ConfirmDisplayNameMessage::class.java, ConfirmDisplayNameDecoder(), ConfirmDisplayNameHandler(), structures)
        put(ItemActionTwoMessage::class.java, ItemActionTwoDecoder(), ItemActionTwoHandler(), structures)
        put(IntegerInputMessage::class.java, IntegerInputDecoder(), IntegerInputHandler(), structures)
        put(SearchedItemMessage::class.java, SearchedItemDecoder(), SearchedItemHandler(), structures)
        put(DropItemMessage::class.java, DropItemDecoder(), DropItemHandler(), structures)
        put(GroundItemActionThreeMessage::class.java, GroundItemActionThreeDecoder(), GroundItemActionThreeHandler(), structures)
        put(NpcAttackActionMessage::class.java, NpcAttackActionDecoder(), NpcAttackActionHandler(), structures)
        put(SpellOnNpcMessage::class.java, SpellOnNpcDecoder(), SpellOnNpcHandler(), structures)
    }

    private fun <T: Message> put(messageType: Class<T>, decoderType: MessageDecoder<T>, handlerType: MessageHandler<T>, structures: MessageStructureSet) {
        val structure = structures.get(messageType)!!
        structure.opcodes.forEach { opcode ->
            decoders[opcode] = decoderType
            handlers[opcode] = handlerType
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun get(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }

    @Suppress("UNCHECKED_CAST")
    fun getHandler(opcode: Int): MessageHandler<Message>? {
        return handlers[opcode] as MessageHandler<Message>?
    }
}