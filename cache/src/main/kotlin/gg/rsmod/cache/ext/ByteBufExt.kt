package gg.rsmod.cache.ext

import io.netty.buffer.ByteBuf

fun ByteBuf.readBigSmart(): Int = if (getByte(readerIndex()) >= 0) {
    readUnsignedShort() and 0xFFFF
} else {
    readInt() and Int.MAX_VALUE
}