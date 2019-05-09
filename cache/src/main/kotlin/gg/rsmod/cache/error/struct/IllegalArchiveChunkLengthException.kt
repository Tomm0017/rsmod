package gg.rsmod.cache.error.struct

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IllegalArchiveChunkLengthException(length: Int)
    : FilestoreException("Invalid archive chunk length: $length")