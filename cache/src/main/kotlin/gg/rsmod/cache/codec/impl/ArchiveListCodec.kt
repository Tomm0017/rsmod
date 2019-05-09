package gg.rsmod.cache.codec.impl

import gg.rsmod.cache.codec.StructCodec
import gg.rsmod.cache.ext.readVersionSmart
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.type.ArchiveVersionType
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ArchiveListCodec(private val versionType: ArchiveVersionType, private val hashedNames: Boolean) : StructCodec<List<Archive>> {

    override fun decode(buffer: ByteBuf): List<Archive> {
        val archiveCount = buffer.readVersionSmart(versionType)

        val ids = arrayOfNulls<Int>(archiveCount)
        val names = arrayOfNulls<Int>(archiveCount)
        val crcs = arrayOfNulls<Int>(archiveCount)
        val versions = arrayOfNulls<Int>(archiveCount)

        var incrementalProtocol = 0
        for (i in 0 until archiveCount) {
            val delta = buffer.readVersionSmart(versionType)
            incrementalProtocol += delta
            ids[i] = incrementalProtocol
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
            versions[i] = buffer.readInt()
        }

        val archives = mutableListOf<Archive>()
        for (i in 0 until archiveCount) {
            val id = ids[i]!!
            val archive = Archive(id, names[i], crcs[i]!!, versions[i]!!)
            archives.add(archive)
        }
        return archives
    }

    override fun encode(buffer: ByteBuf, value: List<Archive>) {
        TODO()
    }
}