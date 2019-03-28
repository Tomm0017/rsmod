package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents a message that defines a player's right click option
 *
 * @param option    The option text
 * @param index     The index of the option
 * @param leftClick If this option should be the default left-click option
 */
data class OpPlayerMessage(val option: String, val index: Int, val leftClick: Boolean) : Message