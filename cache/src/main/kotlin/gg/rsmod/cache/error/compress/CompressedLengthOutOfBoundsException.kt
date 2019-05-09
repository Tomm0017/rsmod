package gg.rsmod.cache.error.compress

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class CompressedLengthOutOfBoundsException(length: Int, maxLength: Int)
    : FilestoreException("Compressed data length out of bounds: $length / $maxLength")