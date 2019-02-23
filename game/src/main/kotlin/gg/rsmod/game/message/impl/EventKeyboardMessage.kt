package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class EventKeyboardMessage(val events: List<KeyEvent>) : Message {

    data class KeyEvent(val key: Int, val lastKeyPress: Int)
}