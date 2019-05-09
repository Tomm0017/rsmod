package gg.rsmod.cache.error.struct

import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class InvalidHashedNamesFlagException(flag: Int)
    : FilestoreException("Invalid flag for hashed names: $flag")