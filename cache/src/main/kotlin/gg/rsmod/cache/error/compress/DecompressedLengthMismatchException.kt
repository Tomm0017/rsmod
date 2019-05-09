package gg.rsmod.cache.error.compress

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class DecompressedLengthMismatchException(actual: Int, expected: Int)
    : FilestoreException("Decompressed data length mismatch. [actual=$actual, expected=$expected]")