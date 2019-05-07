package gg.rsmod.cache

import gg.rsmod.cache.ext.readBigSmart
import io.netty.buffer.ByteBuf
import java.io.RandomAccessFile

/**
yeah it's Index, Archive, Group

index contains the metadata for an archive
then the archive itself contains all of the groups

yeah, but it's actually split up more than that technically still
there's files in groups
im not sure for the name of that "container" (i think is what openrs calls it)
that is all of the files in a single buffer, compressed
 */

/**
 index255 points to the Index data for each Archive in data file
 idx0-n point to where cache data for Archive is in data file
 */

typealias CacheFile = RandomAccessFile

internal const val MASTER_IDX = 255

private fun ByteBuf.readProtocolSmart(protocol: Int): Int = if (protocol >= 7) {
    readBigSmart()
} else {
    readUnsignedShort()
}