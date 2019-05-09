package gg.rsmod.cache

import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.Group
import gg.rsmod.cache.struct.Index
import gg.rsmod.cache.type.GroupType
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

typealias CacheFile = RandomAccessFile

internal const val MASTER_IDX = 255

fun Filestore.load() {
    val fileSystem = loadVFS()
    vfs = fileSystem
}

fun Filestore.getIndex(idx: Int): Index? = vfs.indexes[idx]

fun Filestore.getArchives(index: Index): List<Archive>? = vfs.archives[index]

fun Filestore.getGroups(archive: Archive): List<Group>? = vfs.groups[archive]

fun Filestore.getGroups(groupType: GroupType): List<Group>? {
    val archiveType = groupType.archiveType

    val index = getIndex(archiveType.idx)!!
    val archives = getArchives(index)!!
    val archive = archives.first { it.id == archiveType.idx }

    return getGroups(archive)
}