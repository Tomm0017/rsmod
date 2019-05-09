package gg.rsmod.cache.error.struct

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IllegalArchiveChunkOffsetException(offset: Int)
    : FilestoreException("Invalid archive chunk offset: $offset")