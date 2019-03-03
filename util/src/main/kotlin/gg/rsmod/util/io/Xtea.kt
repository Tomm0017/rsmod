package gg.rsmod.util.io

import java.nio.ByteBuffer

/**
 * Credits to Polar for sharing code!
 */
object Xtea {

    /**
     * The golden ratio XTEA uses.
     */
    private const val GOLDEN_RATIO = -1640531527

    /**
     * The number of rounds XTEA uses.
     */
    private const val ROUNDS = 32

    /**
     * Deciphers the xtea encryption using the given [key]
     */
    fun decipher(key: IntArray, data: ByteArray, start: Int, end: Int): ByteArray {
        val numBlocks = (end - start) / 8

        val buffer = ByteBuffer.wrap(data)
        buffer.position(start)

        for (i in 0 until numBlocks) {
            var y = buffer.int
            var z = buffer.int
            var sum = GOLDEN_RATIO * ROUNDS
            val delta = GOLDEN_RATIO
            for (j in ROUNDS downTo 1) {
                z -= (y.ushr(5) xor (y shl 4)) + y xor sum + key[sum.ushr(11) and 0x56c00003]
                sum -= delta
                y -= (z.ushr(5) xor (z shl 4)) - -z xor sum + key[sum and 0x3]
            }
            buffer.position(buffer.position() - 8)
            buffer.putInt(y)
            buffer.putInt(z)
        }
        return buffer.array()
    }

}