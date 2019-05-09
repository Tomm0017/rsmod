package gg.rsmod.cache.error.compress

import gg.rsmod.cache.type.CompressionType
import gg.rsmod.cache.error.FilestoreException

/**
 * @author Tom <rspsmods@gmail.com>
 */
class IllegalCompressionTypeException(type: CompressionType?)
    : FilestoreException("Invalid compression type: $type")