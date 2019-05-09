package gg.rsmod.cache.codec.impl

import gg.rsmod.cache.codec.StructCodec
import gg.rsmod.cache.error.struct.InvalidHashedNamesFlagException
import gg.rsmod.cache.error.struct.InvalidIndexProtocolException
import gg.rsmod.cache.struct.Index
import gg.rsmod.cache.type.ArchiveVersionType
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
object IndexCodec : StructCodec<Index> {

    override fun decode(buffer: ByteBuf): Index {
        val versionOpcode = buffer.readUnsignedByte().toInt()
        val versionType = ArchiveVersionType.values.firstOrNull { it.opcode == versionOpcode } ?: throw InvalidIndexProtocolException(versionOpcode)

        val version = if (versionType != ArchiveVersionType.NONE) {
            buffer.readInt()
        } else {
            0
        }

        val hashNamesFlag = buffer.readUnsignedByte().toInt()
        val hasHashedNames = (1 and hashNamesFlag) != 0

        if (hashNamesFlag and 1.inv() != 0 || hashNamesFlag and 3.inv() != 0) {
            throw InvalidHashedNamesFlagException(hashNamesFlag)
        }

        val archiveData = ByteArray(buffer.readableBytes())
        buffer.readBytes(archiveData)

        return Index(version, versionType, hasHashedNames, archiveData)
    }

    override fun encode(buffer: ByteBuf, value: Index) {
        TODO()
    }
}