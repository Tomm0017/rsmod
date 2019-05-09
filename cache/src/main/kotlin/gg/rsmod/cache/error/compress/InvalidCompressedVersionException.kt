package gg.rsmod.cache.error.compress

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InvalidCompressedVersionException(version: Int)
    : FilestoreException("Invalid version: $version")