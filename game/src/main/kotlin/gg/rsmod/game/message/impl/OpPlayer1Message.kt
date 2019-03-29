package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * Represents the first player interaction option
 *
 * @param index The index of the player that the client is interacting with
 */
class OpPlayer1Message(val index: Int) : Message