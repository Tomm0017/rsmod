package gg.rsmod.cache.codec.impl

import gg.rsmod.cache.codec.StructCodec
import gg.rsmod.cache.error.struct.IllegalArchiveChunkLengthException
import gg.rsmod.cache.error.struct.IllegalArchiveChunkOffsetException
import gg.rsmod.cache.struct.ArchiveChunk
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
object ArchiveChunkCodec : StructCodec<ArchiveChunk> {

    override fun decode(buffer: ByteBuf): ArchiveChunk {
        val length = buffer.readUnsignedMedium()
        val offset = buffer.readUnsignedMedium()

        if (length == 0) {
            throw IllegalArchiveChunkLengthException(length)
        }

        if (offset == 0) {
            throw IllegalArchiveChunkOffsetException(offset)
        }

        return ArchiveChunk(offset, length)
    }

    override fun encode(buffer: ByteBuf, value: ArchiveChunk) {
        TODO()
    }
}