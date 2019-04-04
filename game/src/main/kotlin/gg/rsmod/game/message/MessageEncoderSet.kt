package gg.rsmod.game.message

import gg.rsmod.game.message.encoder.*
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
        put(RebuildLoginEncoder(), RebuildLoginMessage::class.java)
        put(VarpSmallEncoder(), VarpSmallMessage::class.java)
        put(VarpLargeEncoder(), VarpLargeMessage::class.java)
        put(IfOpenSubEncoder(), IfOpenSubMessage::class.java)
        put(IfCloseSubEncoder(), IfCloseSubMessage::class.java)
        put(IfSetTextEncoder(), IfSetTextMessage::class.java)
        put(IfSetEventsEncoder(), IfSetEventsMessage::class.java)
        put(IfSetHideEncoder(), IfSetHideMessage::class.java)
        put(IfSetAnimEncoder(), IfSetAnimMessage::class.java)
        put(IfSetObjectEncoder(), IfSetObjectMessage::class.java)
        put(IfSetNpcHeadEncoder(), IfSetNpcHeadMessage::class.java)
        put(IfSetPlayerHeadEncoder(), IfSetPlayerHeadMessage::class.java)
        put(IfOpenTopEncoder(), IfOpenTopMessage::class.java)
        put(IfMoveSubEncoder(), IfMoveSubMessage::class.java)
        put(RunClientScriptEncoder(), RunClientScriptMessage::class.java)
        put(RebuildNormalEncoder(), RebuildNormalMessage::class.java)
        put(RebuildRegionEncoder(), RebuildRegionMessage::class.java)
        put(MessageGameEncoder(), MessageGameMessage::class.java)
        put(SetOpPlayerEncoder(), SetOpPlayerMessage::class.java)
        put(LogoutFullEncoder(), LogoutFullMessage::class.java)
        put(UpdateStatEncoder(), UpdateStatMessage::class.java)
        put(UpdateRunEnergyEncoder(), UpdateRunEnergyMessage::class.java)
        put(SetMapFlagEncoder(), SetMapFlagMessage::class.java)
        put(UpdateInvFullEncoder(), UpdateInvFullMessage::class.java)
        put(UpdateZonePartialFollowsEncoder(), UpdateZonePartialFollowsMessage::class.java)
        put(LodAddChangeEncoder(), LocAddChangeMessage::class.java)
        put(LocDelEncoder(), LocDelMessage::class.java)
        put(UpdateZonePartialEnclosedEncoder(), UpdateZonePartialEnclosedMessage::class.java)
        put(UpdateRunWeightEncoder(), UpdateRunWeightMessage::class.java)
        put(ObjAddEncoder(), ObjAddMessage::class.java)
        put(ObjDelEncoder(), ObjDelMessage::class.java)
        put(ObjCountEncoder(), ObjCountMessage::class.java)
        put(MapProjAnimEncoder(), MapProjAnimMessage::class.java)
        put(SynthSoundEncoder(), SynthSoundMessage::class.java)
        put(SoundAreaEncoder(), SoundAreaMessage::class.java)
        put(MidiSongEncoder(), MidiSongMessage::class.java)
        put(OnDialogAbortEncoder(), TriggerOnDialogAbortMessage::class.java)
        put(UpdateRebootTimerEncoder(), UpdateRebootTimerMessage::class.java)
    }

    private fun <T: Message> put(encoder: MessageEncoder<T>, message: Class<out T>) {
        encoders[message] = encoder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Message> get(type: Class<out T>): MessageEncoder<Message>? {
        return encoders[type] as? MessageEncoder<Message>
    }
}