package gg.rsmod.cache.error.struct

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InvalidIndexProtocolException(protocol: Int)
    : FilestoreException("Invalid protocol: $protocol")