package gg.rsmod.game.message

import gg.rsmod.game.message.decoder.*
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
        put(EventAppletFocusMessage::class.java, EventAppletFocusDecoder(), EventAppletFocusHandler(), structures)
        put(EventCameraPositionMessage::class.java, EventCameraPositionDecoder(), EventCameraPositionHandler(), structures)
        put(EventMouseIdleMessage::class.java, EventMouseIdleDecoder(), EventMouseIdleHandler(), structures)
        put(EventKeyboardMessage::class.java, EventKeyboardDecoder(), EventKeyboardHandler(), structures)

        put(DetectModifiedClientMessage::class.java, DetectModifiedClientDecoder(), DetectModifiedClientHandler(), structures)
        put(WindowStatusMessage::class.java, WindowStatusDecoder(), WindowStatusHandler(), structures)

        put(MessagePublicMessage::class.java, MessagePublicDecoder(), MessagePublicHandler(), structures)
        put(UpdateAppearanceMessage::class.java, UpdateAppearanceDecoder(), UpdateAppearanceHandler(), structures)
        put(ClientCheatMessage::class.java, ClientCheatDecoder(), ClientCheatHandler(), structures)
        put(ClanJoinChatLeaveChatMessage::class.java, ClanJoinChatLeaveChatDecoder(), ClanJoinChatLeaveHandler(), structures)

        put(MoveGameClickMessage::class.java, MoveGameClickDecoder(), ClickMapHandler(), structures)
        put(MoveMinimapClickMessage::class.java, MoveMinimapClickDecoder(), ClickMinimapHandler(), structures)
        put(TeleportMessage::class.java, TeleportDecoder(), TeleportHandler(), structures)
        put(ClickWorldMapMessage::class.java, ClickWorldMapDecoder(), ClickWorldMapHandler(), structures)

        put(CloseModalMessage::class.java, CloseModalDecoder(), CloseMainComponentHandler(), structures)
        put(IfButtonMessage::class.java, IfButton1Decoder(), IfButton1Handler(), structures)
        put(IfButtonDMessage::class.java, IfButtonDDecoder(), IfButtonDHandler(), structures)

        put(ResumePauseButtonMessage::class.java, ResumePauseButtonDecoder(), ResumePauseButtonHandler(), structures)
        put(ResumePCountDialogMessage::class.java, ResumePCountDialogDecoder(), ResumePCountDialogHandler(), structures)
        put(ResumePObjDialogMessage::class.java, ResumePObjDialogDecoder(), ResumePObjDialogHandler(), structures)
        put(ResumePStringDialogMessage::class.java, ResumePStringDialogDecoder(), ResumePStringDialogHandler(), structures)

        put(OpLoc1Message::class.java, OpLoc1Decoder(), OpLoc1Handler(), structures)
        put(OpLoc2Message::class.java, OpLoc2Decoder(), OpLoc2Handler(), structures)
        put(OpLoc3Message::class.java, OpLoc3Decoder(), OpLoc3Handler(), structures)
        put(OpLoc4Message::class.java, OpLoc4Decoder(), OpLoc4Handler(), structures)
        put(OpLoc5Message::class.java, OpLoc5Decoder(), OpLoc5Handler(), structures)
        put(OpLoc6Message::class.java, OpLoc6Decoder(), OpLoc6Handler(), structures)

        put(OpHeld1Message::class.java, OpHeld1Decoder(), OpHeld1Handler(), structures)
        put(OpHeld2Message::class.java, OpHeld2Decoder(), OpHeld2Handler(), structures)
        put(OpHeld3Message::class.java, OpHeld3Decoder(), OpHeld3Handler(), structures)
        put(OpHeld4Message::class.java, OpHeld4Decoder(), OpHeld4Handler(), structures)
        put(OpHeld5Message::class.java, OpHeld5Decoder(), OpHeld5Handler(), structures)
        put(OpHeld6Message::class.java, OpHeld6Decoder(), OpHeld6Handler(), structures)
        put(OpHeldDMessage::class.java, OpHeldDDecoder(), OpHeldDHandler(), structures)
        put(OpHeldUMessage::class.java, OpHeldUDecoder(), OpHeldUHandler(), structures)

        put(OpHeldTMessage::class.java, OpHeldTDecoder(), OpHeldTHandler(), structures)
        put(OpLocUMessage::class.java, OpLocUDecoder(), OpLocUHandler(), structures)

        put(OpObj1Message::class.java, OpObj1Decoder(), OpObj1Handler(), structures)
        put(OpObj3Message::class.java, OpObj3Decoder(), OpObj3Handler(), structures)
        put(OpObj4Message::class.java, OpObj4Decoder(), OpObj4Handler(), structures)

        put(OpNpc1Message::class.java, OpNpc1Decoder(), OpNpc1Handler(), structures)
        put(OpNpc2Message::class.java, OpNpc2Decoder(), OpNpc2Handler(), structures)
        put(OpNpc3Message::class.java, OpNpc3Decoder(), OpNpc3Handler(), structures)
        put(OpNpc4Message::class.java, OpNpc4Decoder(), OpNpc4Handler(), structures)
        put(OpNpc5Message::class.java, OpNpc5Decoder(), OpNpc5Handler(), structures)
        put(OpNpc6Message::class.java, OpNpc6Decoder(), OpNpc6Handler(), structures)

        put(OpPlayer1Message::class.java, OpPlayer1Decoder(), OpPlayer1Handler(), structures)
        put(OpPlayer2Message::class.java, OpPlayer2Decoder(), OpPlayer2Handler(), structures)
        put(OpPlayer3Message::class.java, OpPlayer3Decoder(), OpPlayer3Handler(), structures)
        put(OpPlayer4Message::class.java, OpPlayer4Decoder(), OpPlayer4Handler(), structures)
        put(OpPlayer5Message::class.java, OpPlayer5Decoder(), OpPlayer5Handler(), structures)
        put(OpPlayer6Message::class.java, OpPlayer6Decoder(), OpPlayer6Handler(), structures)
        put(OpPlayer7Message::class.java, OpPlayer7Decoder(), OpPlayer7Handler(), structures)
        put(OpPlayer8Message::class.java, OpPlayer8Decoder(), OpPlayer8Handler(), structures)

        put(OpNpcTMessage::class.java, OpNpcTDecoder(), OpNpcTHandler(), structures)
    }

    private fun <T: Message> put(messageType: Class<T>, decoderType: MessageDecoder<T>, handlerType: MessageHandler<T>, structures: MessageStructureSet) {
        val structure = structures.get(messageType) ?: throw RuntimeException("Message structure has not been set in packets file. [message=$messageType]")
        structure.opcodes.forEach { opcode ->
            decoders[opcode] = decoderType
            handlers[opcode] = handlerType
        }
    }

    fun get(opcode: Int): MessageDecoder<*>? {
        return decoders[opcode]
    }

    @Suppress("UNCHECKED_CAST")
    fun getHandler(opcode: Int): MessageHandler<Message>? {
        return handlers[opcode] as MessageHandler<Message>?
    }
}