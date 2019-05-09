package gg.rsmod.cache.codec

import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
interface StructCodec<T> {

    fun decode(buffer: ByteBuf): T

    fun encode(buffer: ByteBuf, value: T)
}
