package gg.rsmod.game.message.impl

import gg.rsmod.game.message.Message

/**
 * @author bmyte <bmytescape@gmail.com>
 */
class CameraShakeMessage(val cam_index: Int, val sinus_x: Int, val amplitude: Int, val sinus_y: Int) : Message