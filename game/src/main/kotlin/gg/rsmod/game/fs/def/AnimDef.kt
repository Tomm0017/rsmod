package gg.rsmod.game.fs.def

import gg.rsmod.game.fs.Definition
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class AnimDef(id: Int) : Definition(id) {

    private lateinit var frameIds: IntArray
    private lateinit var frameLengths: IntArray
    private var priority = -1

    private var lengthInCycles = 0

    val cycleLength: Int get() = lengthInCycles

    override fun decode(buf: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val frameCount = buf.readUnsignedShort()
                var totalFrameLength = 0
                frameIds = IntArray(frameCount)
                frameLengths = IntArray(frameCount)

                for (i in 0 until frameCount) {
                    frameLengths[i] = buf.readUnsignedShort()

                    /**
                     * The last frame on some animations, such as death animations,
                     * are set to be extremely long in length. We don't want to take
                     * that into account when converting to game cycles, so we
                     * only add frame lengths that are below a certain threshold
                     * or if the frame <strong>isn't</strong> the last frame on the
                     * animation.
                     */
                    if (i < frameCount - 1 || frameLengths[i] < 200) {
                        totalFrameLength += frameLengths[i]
                    }
                }

                for (i in 0 until frameCount) {
                    frameIds[i] = buf.readUnsignedShort()
                }

                for (i in 0 until frameCount) {
                    frameIds[i] += buf.readUnsignedShort() shl 16
                }

                /**
                 * Convert the length of the combined frames into game cycles
                 * (assuming a game cycle is 600ms).
                 */
                lengthInCycles = Math.ceil((totalFrameLength * 20.0) / 600.0).toInt()
            }
            2 -> buf.readUnsignedShort()
            3 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedByte()
                }
            }
            5 -> buf.readUnsignedByte()
            6 -> buf.readUnsignedShort()
            7 -> buf.readUnsignedShort()
            8 -> buf.readUnsignedByte()
            9 -> buf.readUnsignedByte()
            10 -> priority = buf.readUnsignedByte().toInt()
            11 -> buf.readUnsignedByte()
            12 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                }
                for (i in 0 until count) {
                    buf.readUnsignedShort()
                }
            }
            13 -> {
                val count = buf.readUnsignedByte()
                for (i in 0 until count) {
                    buf.readMedium()
                }
            }
        }
    }
}