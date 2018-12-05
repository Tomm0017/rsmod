package gg.rsmod.game.message

import gg.rsmod.game.message.codec.MessageEncoder
import gg.rsmod.game.message.codec.encoder.*
import gg.rsmod.game.message.impl.*

/**
 * Stores all the [MessageEncoder]s that are used on the
 * [gg.rsmod.game.service.GameService].
 *
 * @author Tom <rspsmods@gmail.com>
 */
class MessageEncoderSet {

    /**
     * The [MessageEncoder]s stored in respect to their [Message] class.
     */
    private val encoders = hashMapOf<Class<out Message>, MessageEncoder<out Message>>()

    /**
     * Links [Message] classes to their respective [MessageEncoder].
     */
    fun init(structures: MessageStructureSet) {
        encoders[LoginRegionMessage::class.java] = LoginRegionEncoder(structures.get(LoginRegionMessage::class.java)!!)
        encoders[SmallVarpMessage::class.java] = SmallVarpEncoder(structures.get(LoginRegionMessage::class.java)!!)
        encoders[OpenInterfaceMessage::class.java] = OpenInterfaceEncoder(structures.get(OpenInterfaceMessage::class.java)!!)
        encoders[SetDisplayInterfaceMessage::class.java] = DisplayInterfaceEncoder(structures.get(SetDisplayInterfaceMessage::class.java)!!)
        encoders[InvokeScriptMessage::class.java] = InvokeScriptEncoder(structures.get(InvokeScriptMessage::class.java)!!)
        encoders[ChangeStaticRegionMessage::class.java] = ChangeStaticRegionEncoder(structures.get(ChangeStaticRegionMessage::class.java)!!)
        encoders[SendChatboxTextMessage::class.java] = SendChatboxTextEncoder(structures.get(SendChatboxTextMessage::class.java)!!)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Message> get(type: Class<out T>): MessageEncoder<Message>? {
        return encoders[type] as? MessageEncoder<Message>
    }
}