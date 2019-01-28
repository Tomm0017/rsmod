package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SetComponentSettingMessage(val hash: Int, val fromChild: Int, val toChild: Int, val setting: Int) : Message