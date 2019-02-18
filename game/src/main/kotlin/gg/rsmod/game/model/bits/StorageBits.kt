package gg.rsmod.game.model.bits

/**
 * Represents one, or multiple, bits that can be stored inside a [BitStorage].
 *
 * @author Tom <rspsmods@gmail.com>
 */
interface StorageBits {

    val startBit: Int

    val endBit: Int
}