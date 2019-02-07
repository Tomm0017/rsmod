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
     * Links [MessageEncoder] to their respective [Message] class.
     */
    fun init() {
        put(LoginRegionEncoder(), LoginRegionMessage::class.java)
        put(SmallVarpEncoder(), SetSmallVarpMessage::class.java)
        put(BigVarpEncoder(), SetBigVarpMessage::class.java)
        put(OpenComponentEncoder(), OpenComponentMessage::class.java)
        put(CloseComponentEncoder(), CloseComponentMessage::class.java)
        put(IfSetTextEncoder(), IfSetTextMessage::class.java)
        put(IfSetEventsEncoder(), IfSetEventsMessage::class.java)
        put(IfSetHideEncoder(), IfSetHideMessage::class.java)
        put(IfSetAnimEncoder(), IfSetAnimMessage::class.java)
        put(IfSetObjectEncoder(), IfSetObjectMessage::class.java)
        put(IfSetNpcHeadEncoder(), IfSetNpcHeadMessage::class.java)
        put(DisplayComponentEncoder(), SetDisplayComponentMessage::class.java)
        put(RunClientScriptEncoder(), RunClientScriptMessage::class.java)
        put(ChangeStaticRegionEncoder(), ChangeStaticRegionMessage::class.java)
        put(ChatboxTextEncoder(), SendChatboxTextMessage::class.java)
        put(LogoutEncoder(), SendLogoutMessage::class.java)
        put(SendSkillEncoder(), SendSkillMessage::class.java)
        put(RunEnergyEncoder(), SetRunEnergyMessage::class.java)
        put(MinimapMarkerEncoder(), SetMinimapMarkerMessage::class.java)
        put(ItemContainerEncoder(), SetItemContainerMessage::class.java)
        put(MapChunkEncoder(), SetChunkToRegionOffset::class.java)
        put(SpawnObjectEncoder(), SpawnObjectMessage::class.java)
        put(RemoveObjectEncoder(), RemoveObjectMessage::class.java)
        put(SpawnEntityGroupsEncoder(), SpawnEntityGroupsMessage::class.java)
        put(WeightEncoder(), WeightMessage::class.java)
        put(ComponentSwitchEncoder(), ComponentSwitchMessage::class.java)
        put(SpawnGroundItemEncoder(), SpawnGroundItemMessage::class.java)
        put(RemoveGroundItemEncoder(), RemoveGroundItemMessage::class.java)
        put(UpdateGroundItemEncoder(), UpdateGroundItemMessage::class.java)
        put(SpawnProjectileEncoder(), SpawnProjectileMessage::class.java)
        put(PlaySoundEncoder(), PlaySoundMessage::class.java)
        put(PlayAreaSoundEncoder(), PlayAreaSoundMessage::class.java)
    }

    private fun <T: Message> put(encoder: MessageEncoder<T>, message: Class<out T>) {
        encoders[message] = encoder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Message> get(type: Class<out T>): MessageEncoder<Message>? {
        return encoders[type] as? MessageEncoder<Message>
    }
}