package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class SendSkillMessage(val skill: Int, val level: Int, val xp: Int) : Message