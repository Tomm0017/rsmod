package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the second player interaction option
 *
 * @param index The index of the player that the client is interacting with
 */
class OpPlayer2Message(val index: Int) : Message