package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Triston Plummer ("Dread")
 *
 * @param obj the object id
 * @param x the x coordinate of the object
 * @param z the z coordinate of the object
 * @param slot the slot of the used item
 * @param item the item id
 * @param movementType the movement type
 */
data class OpLocUMessage(val obj: Int, val x: Int, val z: Int, val slot: Int, val item: Int, val movementType: Int) : Message