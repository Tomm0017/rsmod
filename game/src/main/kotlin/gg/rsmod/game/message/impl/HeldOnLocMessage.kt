package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * @param obj           The object id
 * @param x             The x coordinate of the object
 * @param z             The z coordinate of the object
 * @param slot          The slot of the used item
 * @param item          The item id
 * @param movementType  The movement type
 */
data class HeldOnLocMessage(val obj: Int, val x: Int, val z: Int, val slot: Int, val item: Int, val movementType: Int) : Message