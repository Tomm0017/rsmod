package gg.rsmod.util

class HuffmanCodec(private val sizes: ByteArray) {

    var masks: IntArray
    var keys: IntArray

    init {
        val i_2 = sizes.size
        masks = IntArray(i_2)
        val ints_3 = IntArray(33)
        keys = IntArray(8)
        var i_4 = 0

        for (i_5 in 0 until i_2) {
            val b_6 = sizes[i_5]
            if (b_6.toInt() != 0) {
                val i_7 = 1 shl 32 - b_6
                val i_8 = ints_3[b_6.toInt()]
                masks[i_5] = i_8
                val i_9: Int
                var i_10: Int
                var i_11: Int
                var i_12: Int
                if (i_8 and i_7 != 0)
                    i_9 = ints_3[b_6 - 1]
                else {
                    i_9 = i_8 or i_7

                    i_10 = b_6 - 1
                    while (i_10 >= 1) {
                        i_11 = ints_3[i_10]
                        if (i_11 != i_8)
                            break

                        i_12 = 1 shl 32 - i_10
                        if (i_11 and i_12 != 0) {
                            ints_3[i_10] = ints_3[i_10 - 1]
                            break
                        }

                        ints_3[i_10] = i_11 or i_12
                        --i_10
                    }
                }

                ints_3[b_6.toInt()] = i_9

                i_10 = b_6 + 1
                while (i_10 <= 32) {
                    if (ints_3[i_10] == i_8)
                        ints_3[i_10] = i_9
                    i_10++
                }

                i_10 = 0

                i_11 = 0
                while (i_11 < b_6) {
                    i_12 = Integer.MIN_VALUE.ushr(i_11)
                    if (i_8 and i_12 != 0) {
                        if (keys[i_10] == 0)
                            keys[i_10] = i_4

                        i_10 = keys[i_10]
                    } else
                        ++i_10

                    if (i_10 >= keys.size) {
                        val ints_13 = IntArray(keys.size * 2)

                        for (i_14 in keys.indices)
                            ints_13[i_14] = keys[i_14]

                        keys = ints_13
                    }

                    i_11++
                }

                keys[i_10] = i_5.inv()
                if (i_10 >= i_4)
                    i_4 = i_10 + 1
            }
        }

    }

    fun compress(text: String, output: ByteArray): Int {
        var key = 0

        val input = text.toByteArray()

        var bitpos = 0
        for (pos in 0 until text.length) {
            val data = input[pos].toInt() and 255
            val size = sizes[data]
            val mask = masks[data]

            if (size.toInt() == 0) {
                throw RuntimeException("No codeword for data value $data")
            }

            var remainder = bitpos and 7
            key = key and (-remainder shr 31)
            var offset = bitpos shr 3
            bitpos += size.toInt()
            val i_41_ = (-1 + (remainder - -size) shr 3) + offset
            remainder += 24
            key = key or mask.ushr(remainder)
            output[offset] = key.toByte()
            if (i_41_.inv() < offset.inv()) {
                remainder -= 8
                key = mask.ushr(remainder)
                output[++offset] = key.toByte()
                if (offset.inv() > i_41_.inv()) {
                    remainder -= 8
                    key = mask.ushr(remainder)
                    output[++offset] = key.toByte()
                    if (offset.inv() > i_41_.inv()) {
                        remainder -= 8
                        key = mask.ushr(remainder)
                        output[++offset] = key.toByte()
                        if (i_41_ > offset) {
                            remainder -= 8
                            key = mask shl -remainder
                            output[++offset] = key.toByte()
                        }
                    }
                }
            }
        }

        return 7 + bitpos shr 3
    }

    fun decompress(compressed: ByteArray, decompressed: ByteArray, decompressedLength: Int): Int {
        var decompressedLen = decompressedLength
        val i_2 = 0
        var i_4 = 0
        if (decompressedLength == 0)
            return 0
        else {
            var i_7 = 0
            decompressedLen += i_4
            var i_8 = i_2

            while (true) {
                val b_9 = compressed[i_8]
                if (b_9 < 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                var i_10: Int
                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x40 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x20 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x10 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x8 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x4 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x2 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                if (b_9.toInt() and 0x1 != 0)
                    i_7 = keys[i_7]
                else
                    ++i_7

                i_10 = keys[i_7]
                if (i_10 < 0) {
                    decompressed[i_4++] = i_10.inv().toByte()
                    if (i_4 >= decompressedLength)
                        break

                    i_7 = 0
                }

                ++i_8
            }

            return i_8 + 1 - i_2
        }
    }
}