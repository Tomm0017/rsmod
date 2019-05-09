package gg.rsmod.cache.codec.impl

import gg.rsmod.cache.codec.StructCodec
import gg.rsmod.cache.ext.readProtocolSmart
import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.Group
import io.netty.buffer.ByteBuf

/**
 * @author Tom <rspsmods@gmail.com>
 */
class ArchiveGroupListCodec(private val archives: List<Archive>, private val protocol: Int, private val hashedNames: Boolean)
    : StructCodec<Map<Archive, List<Group>>> {

    override fun decode(buffer: ByteBuf): Map<Archive, List<Group>> {
        val archiveCount = archives.size

        val groupCount = arrayOfNulls<Int>(archiveCount)
        val archiveGroupIds = mutableMapOf<Archive, MutableMap<Int, Int>>()
        val archiveGroupNames = mutableMapOf<Archive, MutableMap<Int, Int>>()

        for (i in 0 until archiveCount) {
            groupCount[i] = buffer.readProtocolSmart(protocol)
        }

        for (i in 0 until archiveCount) {
            val count = groupCount[i]!!
            val archive = archives[i]
            val ids = archiveGroupIds.computeIfAbsent(archive) { mutableMapOf() }

            var incrementalProtocol = 0
            for (j in 0 until count) {
                incrementalProtocol += protocol
                ids[j] = buffer.readProtocolSmart(incrementalProtocol)
            }
        }

        if (hashedNames) {
            for (i in 0 until archiveCount) {
                val count = groupCount[i]!!
                val archive = archives[i]
                val names = archiveGroupNames.computeIfAbsent(archive) { mutableMapOf() }

                for (j in 0 until count) {
                    names[j] = buffer.readInt()
                }
            }
        }

        val archiveGroups = mutableMapOf<Archive, MutableList<Group>>()
        for (i in 0 until archiveCount) {
            val archive = archives[i]
            val groups = archiveGroups.computeIfAbsent(archive) { mutableListOf() }

            val count = groupCount[i]!!
            val ids = archiveGroupIds[archive]!!
            val names = archiveGroupNames[archive]

            for (j in 0 until count) {
                val id = ids[j]!!
                val name = names?.get(j)
                val group = Group(id, name)
                groups.add(group)
            }
        }

        return archiveGroups
    }

    override fun encode(buffer: ByteBuf, value: Map<Archive, List<Group>>) {
        TODO()
    }
}