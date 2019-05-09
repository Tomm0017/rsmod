package gg.rsmod.cache.codec.impl

import gg.rsmod.cache.codec.StructCodec
import gg.rsmod.cache.ext.readProtocolSmart
import gg.rsmod.cache.struct.Archive
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ArchiveListCodec(private val protocol: Int, private val hashedNames: Boolean) : StructCodec<List<Archive>> {

    override fun decode(buffer: ByteBuf): List<Archive> {
        val archiveCount = buffer.readProtocolSmart(protocol)

        val ids = arrayOfNulls<Int>(archiveCount)
        val names = arrayOfNulls<Int>(archiveCount)
        val crcs = arrayOfNulls<Int>(archiveCount)
        val revisions = arrayOfNulls<Int>(archiveCount)

        var incrementalProtocol = 0
        for (i in 0 until archiveCount) {
            incrementalProtocol += protocol
            ids[i] = buffer.readProtocolSmart(incrementalProtocol)
        }

        if (hashedNames) {
            for (i in 0 until archiveCount) {
                names[i] = buffer.readInt()
            }
        }

        for (i in 0 until archiveCount) {
            crcs[i] = buffer.readInt()
        }

        for (i in 0 until archiveCount) {
            revisions[i] = buffer.readInt()
        }

        val archives = mutableListOf<Archive>()
        for (i in 0 until archiveCount) {
            val id = ids[i]!!
            val archive = Archive(id, names[i], crcs[i]!!, revisions[i]!!)
            archives.add(archive)
        }
        return archives
    }

    override fun encode(buffer: ByteBuf, value: List<Archive>) {
        TODO()
    }
}