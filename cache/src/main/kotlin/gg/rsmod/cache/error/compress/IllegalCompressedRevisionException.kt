package gg.rsmod.cache.error.compress

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IllegalCompressedRevisionException(revision: Int)
    : FilestoreException("Invalid revision: $revision")