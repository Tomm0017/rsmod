package gg.rsmod.game.sync

import mu.KLogging
import java.util.concurrent.Phaser

/**
 * @author Tom <rspsmods@gmail.com>
 */
class PhasedSynchronizationTask(private val phaser: Phaser) {

    companion object: KLogging()
}