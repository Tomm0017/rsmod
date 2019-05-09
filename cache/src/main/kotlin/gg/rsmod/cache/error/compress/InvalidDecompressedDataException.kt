package gg.rsmod.cache.error.compress

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InvalidDecompressedDataException
    : FilestoreException("Decompressed data is invalid (null).")