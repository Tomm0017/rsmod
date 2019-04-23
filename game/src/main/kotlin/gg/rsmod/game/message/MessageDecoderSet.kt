package gg.rsmod.game.message

import gg.rsmod.game.message.decoder.ClanJoinChatLeaveChatDecoder
import gg.rsmod.game.message.decoder.ClickWorldMapDecoder
import gg.rsmod.game.message.decoder.ClientCheatDecoder
import gg.rsmod.game.message.decoder.CloseModalDecoder
import gg.rsmod.game.message.decoder.DetectModifiedClientDecoder
import gg.rsmod.game.message.decoder.EventAppletFocusDecoder
import gg.rsmod.game.message.decoder.EventCameraPositionDecoder
import gg.rsmod.game.message.decoder.EventKeyboardDecoder
import gg.rsmod.game.message.decoder.EventMouseIdleDecoder
import gg.rsmod.game.message.decoder.IfButton1Decoder
import gg.rsmod.game.message.decoder.IfButtonDDecoder
import gg.rsmod.game.message.decoder.MapBuildCompleteDecoder
import gg.rsmod.game.message.decoder.MessagePublicDecoder
import gg.rsmod.game.message.decoder.MoveGameClickDecoder
import gg.rsmod.game.message.decoder.MoveMinimapClickDecoder
import gg.rsmod.game.message.decoder.OpHeld1Decoder
import gg.rsmod.game.message.decoder.OpHeld2Decoder
import gg.rsmod.game.message.decoder.OpHeld3Decoder
import gg.rsmod.game.message.decoder.OpHeld4Decoder
import gg.rsmod.game.message.decoder.OpHeld5Decoder
import gg.rsmod.game.message.decoder.OpHeld6Decoder
import gg.rsmod.game.message.decoder.OpHeldDDecoder
import gg.rsmod.game.message.decoder.OpHeldTDecoder
import gg.rsmod.game.message.decoder.OpHeldUDecoder
import gg.rsmod.game.message.decoder.OpLoc1Decoder
import gg.rsmod.game.message.decoder.OpLoc2Decoder
import gg.rsmod.game.message.decoder.OpLoc3Decoder
import gg.rsmod.game.message.decoder.OpLoc4Decoder
import gg.rsmod.game.message.decoder.OpLoc5Decoder
import gg.rsmod.game.message.decoder.OpLoc6Decoder
import gg.rsmod.game.message.decoder.OpLocUDecoder
import gg.rsmod.game.message.decoder.OpNpc1Decoder
import gg.rsmod.game.message.decoder.OpNpc2Decoder
import gg.rsmod.game.message.decoder.OpNpc3Decoder
import gg.rsmod.game.message.decoder.OpNpc4Decoder
import gg.rsmod.game.message.decoder.OpNpc5Decoder
import gg.rsmod.game.message.decoder.OpNpc6Decoder
import gg.rsmod.game.message.decoder.OpNpcTDecoder
import gg.rsmod.game.message.decoder.OpObj1Decoder
import gg.rsmod.game.message.decoder.OpObj3Decoder
import gg.rsmod.game.message.decoder.OpObj4Decoder
import gg.rsmod.game.message.decoder.OpPlayer1Decoder
import gg.rsmod.game.message.decoder.OpPlayer2Decoder
import gg.rsmod.game.message.decoder.OpPlayer3Decoder
import gg.rsmod.game.message.decoder.OpPlayer4Decoder
import gg.rsmod.game.message.decoder.OpPlayer5Decoder
import gg.rsmod.game.message.decoder.OpPlayer6Decoder
import gg.rsmod.game.message.decoder.OpPlayer7Decoder
import gg.rsmod.game.message.decoder.OpPlayer8Decoder
import gg.rsmod.game.message.decoder.ResumePCountDialogDecoder
import gg.rsmod.game.message.decoder.ResumePObjDialogDecoder
import gg.rsmod.game.message.decoder.ResumePStringDialogDecoder
import gg.rsmod.game.message.decoder.ResumePauseButtonDecoder
import gg.rsmod.game.message.decoder.TeleportDecoder
import gg.rsmod.game.message.decoder.UpdateAppearanceDecoder
import gg.rsmod.game.message.decoder.WindowStatusDecoder
import gg.rsmod.game.message.handler.ClanJoinChatLeaveHandler
import gg.rsmod.game.message.handler.ClickMapHandler
import gg.rsmod.game.message.handler.ClickMinimapHandler
import gg.rsmod.game.message.handler.ClickWorldMapHandler
import gg.rsmod.game.message.handler.ClientCheatHandler
import gg.rsmod.game.message.handler.CloseMainComponentHandler
import gg.rsmod.game.message.handler.DetectModifiedClientHandler
import gg.rsmod.game.message.handler.EventAppletFocusHandler
import gg.rsmod.game.message.handler.EventCameraPositionHandler
import gg.rsmod.game.message.handler.EventKeyboardHandler
import gg.rsmod.game.message.handler.EventMouseIdleHandler
import gg.rsmod.game.message.handler.IfButton1Handler
import gg.rsmod.game.message.handler.IfButtonDHandler
import gg.rsmod.game.message.handler.MapBuildCompleteHandler
import gg.rsmod.game.message.handler.MessagePublicHandler
import gg.rsmod.game.message.handler.OpHeld1Handler
import gg.rsmod.game.message.handler.OpHeld2Handler
import gg.rsmod.game.message.handler.OpHeld3Handler
import gg.rsmod.game.message.handler.OpHeld4Handler
import gg.rsmod.game.message.handler.OpHeld5Handler
import gg.rsmod.game.message.handler.OpHeld6Handler
import gg.rsmod.game.message.handler.OpHeldDHandler
import gg.rsmod.game.message.handler.OpHeldTHandler
import gg.rsmod.game.message.handler.OpHeldUHandler
import gg.rsmod.game.message.handler.OpLoc1Handler
import gg.rsmod.game.message.handler.OpLoc2Handler
import gg.rsmod.game.message.handler.OpLoc3Handler
import gg.rsmod.game.message.handler.OpLoc4Handler
import gg.rsmod.game.message.handler.OpLoc5Handler
import gg.rsmod.game.message.handler.OpLoc6Handler
import gg.rsmod.game.message.handler.OpLocUHandler
import gg.rsmod.game.message.handler.OpNpc1Handler
import gg.rsmod.game.message.handler.OpNpc2Handler
import gg.rsmod.game.message.handler.OpNpc3Handler
import gg.rsmod.game.message.handler.OpNpc4Handler
import gg.rsmod.game.message.handler.OpNpc5Handler
import gg.rsmod.game.message.handler.OpNpc6Handler
import gg.rsmod.game.message.handler.OpNpcTHandler
import gg.rsmod.game.message.handler.OpObj1Handler
import gg.rsmod.game.message.handler.OpObj3Handler
import gg.rsmod.game.message.handler.OpObj4Handler
import gg.rsmod.game.message.handler.OpPlayer1Handler
import gg.rsmod.game.message.handler.OpPlayer2Handler
import gg.rsmod.game.message.handler.OpPlayer3Handler
import gg.rsmod.game.message.handler.OpPlayer4Handler
import gg.rsmod.game.message.handler.OpPlayer5Handler
import gg.rsmod.game.message.handler.OpPlayer6Handler
import gg.rsmod.game.message.handler.OpPlayer7Handler
import gg.rsmod.game.message.handler.OpPlayer8Handler
import gg.rsmod.game.message.handler.ResumePCountDialogHandler
import gg.rsmod.game.message.handler.ResumePObjDialogHandler
import gg.rsmod.game.message.handler.ResumePStringDialogHandler
import gg.rsmod.game.message.handler.ResumePauseButtonHandler
import gg.rsmod.game.message.handler.TeleportHandler
import gg.rsmod.game.message.handler.UpdateAppearanceHandler
import gg.rsmod.game.message.handler.WindowStatusHandler
import gg.rsmod.game.message.impl.ClanJoinChatLeaveChatMessage
import gg.rsmod.game.message.impl.ClickWorldMapMessage
import gg.rsmod.game.message.impl.ClientCheatMessage
import gg.rsmod.game.message.impl.CloseModalMessage
import gg.rsmod.game.message.impl.DetectModifiedClientMessage
import gg.rsmod.game.message.impl.EventAppletFocusMessage
import gg.rsmod.game.message.impl.EventCameraPositionMessage
import gg.rsmod.game.message.impl.EventKeyboardMessage
import gg.rsmod.game.message.impl.EventMouseIdleMessage
import gg.rsmod.game.message.impl.IfButtonDMessage
import gg.rsmod.game.message.impl.IfButtonMessage
import gg.rsmod.game.message.impl.MapBuildCompleteMessage
import gg.rsmod.game.message.impl.MessagePublicMessage
import gg.rsmod.game.message.impl.MoveGameClickMessage
import gg.rsmod.game.message.impl.MoveMinimapClickMessage
import gg.rsmod.game.message.impl.OpHeld1Message
import gg.rsmod.game.message.impl.OpHeld2Message
import gg.rsmod.game.message.impl.OpHeld3Message
import gg.rsmod.game.message.impl.OpHeld4Message
import gg.rsmod.game.message.impl.OpHeld5Message
import gg.rsmod.game.message.impl.OpHeld6Message
import gg.rsmod.game.message.impl.OpHeldDMessage
import gg.rsmod.game.message.impl.OpHeldTMessage
import gg.rsmod.game.message.impl.OpHeldUMessage
import gg.rsmod.game.message.impl.OpLoc1Message
import gg.rsmod.game.message.impl.OpLoc2Message
import gg.rsmod.game.message.impl.OpLoc3Message
import gg.rsmod.game.message.impl.OpLoc4Message
import gg.rsmod.game.message.impl.OpLoc5Message
import gg.rsmod.game.message.impl.OpLoc6Message
import gg.rsmod.game.message.impl.OpLocUMessage
import gg.rsmod.game.message.impl.OpNpc1Message
import gg.rsmod.game.message.impl.OpNpc2Message
import gg.rsmod.game.message.impl.OpNpc3Message
import gg.rsmod.game.message.impl.OpNpc4Message
import gg.rsmod.game.message.impl.OpNpc5Message
import gg.rsmod.game.message.impl.OpNpc6Message
import gg.rsmod.game.message.impl.OpNpcTMessage
import gg.rsmod.game.message.impl.OpObj1Message
import gg.rsmod.game.message.impl.OpObj3Message
import gg.rsmod.game.message.impl.OpObj4Message
import gg.rsmod.game.message.impl.OpPlayer1Message
import gg.rsmod.game.message.impl.OpPlayer2Message
import gg.rsmod.game.message.impl.OpPlayer3Message
import gg.rsmod.game.message.impl.OpPlayer4Message
import gg.rsmod.game.message.impl.OpPlayer5Message
import gg.rsmod.game.message.impl.OpPlayer6Message
import gg.rsmod.game.message.impl.OpPlayer7Message
import gg.rsmod.game.message.impl.OpPlayer8Message
import gg.rsmod.game.message.impl.ResumePCountDialogMessage
import gg.rsmod.game.message.impl.ResumePObjDialogMessage
import gg.rsmod.game.message.impl.ResumePStringDialogMessage
import gg.rsmod.game.message.impl.ResumePauseButtonMessage
import gg.rsmod.game.message.impl.TeleportMessage
import gg.rsmod.game.message.impl.UpdateAppearanceMessage
import gg.rsmod.game.message.impl.WindowStatusMessage

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

        put(MapBuildCompleteMessage::class.java, MapBuildCompleteDecoder(), MapBuildCompleteHandler(), structures)
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

    private fun <T : Message> put(messageType: Class<T>, decoderType: MessageDecoder<T>, handlerType: MessageHandler<T>, structures: MessageStructureSet) {
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