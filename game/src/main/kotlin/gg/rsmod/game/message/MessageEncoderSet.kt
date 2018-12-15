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
        put(LoginRegionMessage::class.java, LoginRegionEncoder(structures.get(LoginRegionMessage::class.java)!!))
        put(SetSmallVarpMessage::class.java, SmallVarpEncoder(structures.get(SetSmallVarpMessage::class.java)!!))
        put(SetBigVarpMessage::class.java, BigVarpEncoder(structures.get(SetBigVarpMessage::class.java)!!))
        put(OpenInterfaceMessage::class.java, OpenInterfaceEncoder(structures.get(OpenInterfaceMessage::class.java)!!))
        put(CloseInterfaceMessage::class.java, CloseInterfaceEncoder(structures.get(CloseInterfaceMessage::class.java)!!))
        put(SetInterfaceTextMessage::class.java, InterfaceTextEncoder(structures.get(SetInterfaceTextMessage::class.java)!!))
        put(SetInterfaceSettingMessage::class.java, InterfaceSettingEncoder(structures.get(SetInterfaceSettingMessage::class.java)!!))
        put(SetInterfaceHiddenMessage::class.java, InterfaceHiddenEncoder(structures.get(SetInterfaceHiddenMessage::class.java)!!))
        put(SetInterfaceAnimationMessage::class.java, InterfaceAnimationEncoder(structures.get(SetInterfaceAnimationMessage::class.java)!!))
        put(SetInterfaceItemMessage::class.java, InterfaceItemEncoder(structures.get(SetInterfaceItemMessage::class.java)!!))
        put(SetInterfaceNpcMessage::class.java, InterfaceNpcEncoder(structures.get(SetInterfaceNpcMessage::class.java)!!))
        put(SetDisplayInterfaceMessage::class.java, DisplayInterfaceEncoder(structures.get(SetDisplayInterfaceMessage::class.java)!!))
        put(InvokeScriptMessage::class.java, InvokeScriptEncoder(structures.get(InvokeScriptMessage::class.java)!!))
        put(ChangeStaticRegionMessage::class.java, ChangeStaticRegionEncoder(structures.get(ChangeStaticRegionMessage::class.java)!!))
        put(SendChatboxTextMessage::class.java, ChatboxTextEncoder(structures.get(SendChatboxTextMessage::class.java)!!))
        put(SendLogoutMessage::class.java, LogoutEncoder(structures.get(SendLogoutMessage::class.java)!!))
        put(SendSkillMessage::class.java, SendSkillEncoder(structures.get(SendSkillMessage::class.java)!!))
        put(SetRunEnergyMessage::class.java, RunEnergyEncoder(structures.get(SetRunEnergyMessage::class.java)!!))
        put(SetMinimapMarkerMessage::class.java, MinimapMarkerEncoder(structures.get(SetMinimapMarkerMessage::class.java)!!))
        put(SetItemContainerMessage::class.java, ItemContainerEncoder(structures.get(SetItemContainerMessage::class.java)!!))
        put(SetMapChunkMessage::class.java, MapChunkEncoder(structures.get(SetMapChunkMessage::class.java)!!))
        put(SpawnObjectMessage::class.java, SpawnObjectEncoder(structures.get(SpawnObjectMessage::class.java)!!))
        put(RemoveObjectMessage::class.java, RemoveObjectEncoder(structures.get(RemoveObjectMessage::class.java)!!))
    }

    private fun <T: Message> put(message: Class<out T>, encoder: MessageEncoder<T>) {
        encoders[message] = encoder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Message> get(type: Class<out T>): MessageEncoder<Message>? {
        return encoders[type] as? MessageEncoder<Message>
    }
}