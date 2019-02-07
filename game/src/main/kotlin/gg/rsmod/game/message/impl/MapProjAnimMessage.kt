package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author Tom <rspsmods@gmail.com>
 */
data class MapProjAnimMessage(val start: Int, val pawnTargetIndex: Int, val offsetX: Int, val offsetZ: Int,
                              val gfx: Int, val startHeight: Int, val endHeight: Int, val delay: Int,
                              val lifespan: Int, val angle: Int, val steepness: Int) : Message