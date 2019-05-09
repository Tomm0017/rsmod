package gg.rsmod.cache

import gg.rsmod.cache.struct.Archive
import gg.rsmod.cache.struct.Group
import gg.rsmod.cache.struct.Index

/**
 * @author Tom <rspsmods@gmail.com>
 */
internal class VirtualFileSystem(
        val indexes: Map<Int, Index>,
        val archives: Map<Index, List<Archive>>,
        val groups: Map<Archive, List<Group>>
)