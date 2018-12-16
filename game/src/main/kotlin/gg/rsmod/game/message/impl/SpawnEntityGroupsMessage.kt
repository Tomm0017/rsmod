package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message
import gg.rsmod.game.message.MessageEncoderSet
import gg.rsmod.game.message.MessageStructureSet

/**
 * @author Tom <rspsmods@gmail.com>
 */
class SpawnEntityGroupsMessage(val x: Int, val z: Int, val encoders: MessageEncoderSet, val structures: MessageStructureSet,
                               vararg val messages: EntityGroupMessage) : Message